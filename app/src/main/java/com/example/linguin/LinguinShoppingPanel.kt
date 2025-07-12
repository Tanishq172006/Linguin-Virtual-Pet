package com.example.myapplication.Linguin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R


class LinguinShoppingPanel : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {

        val view=  inflater.inflate(R.layout.fragment_linguin_shopping_panel , container , false)
        return view
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        val datalist = ArrayList<LinguinShopData>()


        val icon1 = LinguinShopData(R.drawable.macreal, "Mac Toy")
        val icon2 = LinguinShopData(R.drawable.linuxtoyreal, "Tux Penguin")
        val icon3 = LinguinShopData(R.drawable.archreal, "Arch Toy")
        val icon4 = LinguinShopData(R.drawable.sandwichfood, "SudoSandwich")
        val icon5 = LinguinShopData(R.drawable.topramenreal, "Top Ramen")
        val icon6 = LinguinShopData(R.drawable.shellfishimg, "Shell Fish")
        val icon7 = LinguinShopData(R.drawable.applepie, "Apt Pie")
        val icon8 = LinguinShopData(R.drawable.windowsreal, "Windows Toy")


        datalist.add(icon1)
        datalist.add(icon2)
        datalist.add(icon3)
        datalist.add(icon4)
        datalist.add(icon5)
        datalist.add(icon6)
        datalist.add(icon7)
        datalist.add(icon8)

        val recyclerView: RecyclerView = view.findViewById(R.id.LinguinRecycler)
        val linguinAdapter = LinguinShoppingRecyclerAdapter(datalist){ selectedItem->
            val action = LinguinShoppingPanelDirections.actionLinguinShoppingPanelToLiguinMainFragment(selectedItem.Itemtxt)
            findNavController().navigate(action)

        }




        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = linguinAdapter





    }






}
