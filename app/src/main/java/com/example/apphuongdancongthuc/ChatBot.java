package com.example.apphuongdancongthuc;


import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class ChatBot extends AppCompatActivity {

    private EditText edtInput;
    private ImageButton imbSend;
    private ImageButton imbGallery;

    private String data;
    private MessageRVAdapter messageRVAdapter;
    private RecyclerView rcvChatBot;
    private RecyclerView rcvImages;
    private ArrayList<MessageModel> messageModelArrayList;
    private Database database;
    private String username;

    private Gemini gemini;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private ArrayList<Uri> imageUris;

    private RcvListImageAdapter rcvListImageAdapter;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_delete) {
                showDeleteDialog();
                return true;
            }
            return false;
        });
        edtInput = findViewById(R.id.edtInput);
        imbSend = findViewById(R.id.imbSend);
        imbGallery = findViewById(R.id.imbGallery);
        rcvChatBot = findViewById(R.id.rcvChatBot);
        rcvImages = findViewById(R.id.rcvImages);
        imbSend.setOnClickListener(v -> Send());
        imbGallery.setOnClickListener(v -> pickImages());
        messageModelArrayList = new ArrayList<>();
        messageRVAdapter = new MessageRVAdapter(messageModelArrayList, this);
        rcvChatBot.setAdapter(messageRVAdapter);
        database = new Database(this, "banhang.db", null, 1);
        //database.QueryData("DROP TABLE messages");
        database.QueryData("CREATE TABLE IF NOT EXISTS messages (id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT NOT NULL, userId TEXT NOT NULL, productId TEXT, role TEXT NOT NULL,images TEXT)");
        gemini = Gemini.getInstance();
        loadHistoryMessage();
        loadData();
        imageUris = new ArrayList<>();
        rcvListImageAdapter = new RcvListImageAdapter(this, imageUris);
        rcvImages.setAdapter(rcvListImageAdapter);
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        String save = saveImageToInternalStorage(uri);
                        if (save == null) return;
                        imageUris.add(Uri.parse(save));
                        rcvListImageAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa tất cả tin nhắn")
                .setMessage("Bạn có chắc chắn muốn xóa?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    clearHistory();
                    Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void clearHistory() {
        database.QueryData("DELETE FROM messages");
        messageModelArrayList.clear();
        ;
        messageRVAdapter.notifyDataSetChanged();
    }

    private void pickImages() {

        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private Boolean requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
                return false;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        return true;
    }


    private void loadHistoryMessage() {
        SharedPreferences sharedPre = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        username = sharedPre.getString("tendn", null);
        Cursor cursor = database.GetData("SELECT * from messages  WHERE userId = '" + username + "' ORDER BY id ASC");
        if (cursor == null || cursor.getCount() == 0) {
            return;
        }
        data = "";
        cursor.moveToFirst();
        do {
            int id = cursor.getInt(0);
            String message = cursor.getString(1);
            String userId = cursor.getString(2);
            String productId = cursor.getString(3);
            String role = cursor.getString(4);
            String images = cursor.getString(5);
            MessageModel model = new MessageModel(id, message, userId, productId, role, images);
            messageModelArrayList.add(model);
            cursor.moveToNext();
        } while (!cursor.isAfterLast());
        if (messageModelArrayList.isEmpty()) {
            MessageModel model = new MessageModel("Xin chào, tôi có thể giúp gì cho bạn?", username, null, "bot", null);
            messageModelArrayList.add(model);
        }
        messageRVAdapter.notifyDataSetChanged();
        rcvChatBot.scrollToPosition(messageRVAdapter.getItemCount() - 1);
        gemini.start(messageModelArrayList);
    }

    private void loadData() {

        Cursor cursor = database.GetData("SELECT * from sanpham");
        if (cursor == null || cursor.getCount() == 0) {
            return;
        }
        data = "";
        cursor.moveToFirst();
        do {
            String masp = cursor.getInt(cursor.getColumnIndexOrThrow("masp")) + "";
            String tensp = cursor.getString(cursor.getColumnIndexOrThrow("tensp"));
            String nguyenlieu = cursor.getString(cursor.getColumnIndexOrThrow("nguyenlieu"));
            String cachlam = cursor.getString(cursor.getColumnIndexOrThrow("cachlam"));
            String ghichu = cursor.getString(cursor.getColumnIndexOrThrow("ghichu"));
            String nguoidang = cursor.getString(cursor.getColumnIndexOrThrow("nguoidang"));
            String mansp = cursor.getString(cursor.getColumnIndexOrThrow("maso"));
            byte[] anh = cursor.getBlob(cursor.getColumnIndexOrThrow("anh"));

            SanPham sanPham = new SanPham(masp, tensp, nguyenlieu, cachlam, ghichu, nguoidang, mansp, anh);
            data += sanPham.toString() + ", ";
            cursor.moveToNext();
        } while (!cursor.isAfterLast());
    }

    private void Send() {
        if (!imageUris.isEmpty()) {
            Boolean check = requestPermission();
            if (!check) return;
        }
        String text = edtInput.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập văn bản", Toast.LENGTH_SHORT).show();
            return;
        }
        if (data == null || data.isEmpty()) {
            Toast.makeText(this, "Không có món ăn nào", Toast.LENGTH_SHORT).show();
            return;
        }
        edtInput.setText("");
        String images = UriConverter.fromUriList(imageUris);
        ArrayList<Uri> u = new ArrayList<>(imageUris);
        imageUris.clear();
        MessageModel modelU = new MessageModel(text, username, null, "user", images);
        rcvListImageAdapter.notifyDataSetChanged();
        messageModelArrayList.add(modelU);
        MessageModel modelLoading = new MessageModel(null, username, null, "bot", null);
        messageModelArrayList.add(modelLoading);
        messageRVAdapter.notifyDataSetChanged();
        rcvChatBot.scrollToPosition(messageRVAdapter.getItemCount() - 1);
        addMessage(modelU);
        try {
            Thread thread = new Thread(() -> {
                GeminiResponse rs = gemini.generateContent(text, data, u);
                MessageModel modelB = new MessageModel(rs.getText(), username, "", "bot", null);
                runOnUiThread(() -> {
                    messageModelArrayList.add(modelB);
                    messageModelArrayList.removeIf((messageModel -> messageModel.getMessage() == null));
                    messageRVAdapter.notifyDataSetChanged();
                    rcvChatBot.scrollToPosition(messageRVAdapter.getItemCount() - 1);
                });
                addMessage(modelB);
                gemini.start(messageModelArrayList);
            });
            thread.start();
        } catch (Exception e) {
            messageModelArrayList.removeIf((messageModel -> messageModel.getMessage() == null));
            messageRVAdapter.notifyDataSetChanged();
            rcvChatBot.scrollToPosition(messageRVAdapter.getItemCount() - 1);
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void addMessage(MessageModel model) {
        String insertQuery = "INSERT INTO messages (message, userId, productId, role, images) VALUES (" + model.toString() + ")";
        database.QueryData(insertQuery);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Send();
            } else {
                Log.d("Permission", "Quyền bị từ chối");
            }
        }
    }

    private String saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File file = new File(getFilesDir(), "image_" + System.currentTimeMillis() + ".jpg");
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            Log.d("PhotoPicker", "Lưu ảnh thành công: " + file.getAbsolutePath());
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
