package com.example.linguin

import ChatViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.RetroChat.ChatAdapter


class LinguinChat : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_linguin_chat , container , false)
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewMessages)
        val adapter = ChatAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

         val viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            adapter.updateMessages(messages)
            recyclerView.scrollToPosition(messages.size - 1)
        }


        val input = view.findViewById<EditText>(R.id.editTextMessage)
        val send = view.findViewById<Button>(R.id.buttonSend)

        send.setOnClickListener {
            val msg = input.text.toString()
            if (msg.isNotBlank()) {
                viewModel.sendMessage(msg)
                input.text.clear()
            }
        }

    }
}