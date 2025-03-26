package com.example.apphuongdancongthuc

import android.net.Uri
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlobPart
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.Part
import com.google.ai.client.generativeai.type.TextPart
import kotlinx.coroutines.runBlocking
import java.io.File


class Gemini {

    companion object {
        @JvmStatic
        fun getInstance(): Gemini = Gemini()

        private val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-pro-latest",
            apiKey = "",
        )

    }

    fun start(histories: List<MessageModel>) {
        val contents = arrayListOf<Content>()
        histories.forEach { e ->
            if(e.message != null) {
                contents.add(
                    Content(
                        role = if (e.isUser) "User" else "Bot",
                        parts = listOf(TextPart(e.message)),
                    )
                )
            }
        }
        generativeModel.startChat(contents)
    }


    fun generateContent(text: String, data: String, images: List<Uri>): GeminiResponse {
        val response: GenerateContentResponse
        val question =
            "This is the data I provide to you: '$data'. Please act as a real chef and help the user choose a menu by answering the following question: '$text?'. If you don't have a suitable answer, say 'Your question is beyond my scope.' If you find relevant data, suggest an option to the user along with a product ID marked by #. For example: 'You can try this roast chicken.#productID'. Make sure to respond in the same language as the user's question."
        runBlocking {
            val parts: ArrayList<Part> = arrayListOf(TextPart(question))
            images.forEach { e ->
                run {
                    val file = File(e.path!!)
                    val bytes = file.readBytes();
                    parts.add(BlobPart(mimeType = "image/jpeg", blob = bytes))
                }
            }
            response = generativeModel.generateContent(Content(parts = parts))
        }
        return GeminiResponse(
            response.text,
        )
    }




}

data class GeminiResponse(
    val text: String?,
)


