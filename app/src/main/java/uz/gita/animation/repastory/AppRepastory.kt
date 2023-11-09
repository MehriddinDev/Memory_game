package uz.gita.animation.repastory

import uz.gita.animation.R
import uz.gita.animation.data.CardData
import kotlin.random.Random

class AppRepastory{

    private val cardList = arrayListOf<CardData>()
    private val imgsList = ArrayList<Int>()
    init {

        cardList.add(CardData(R.drawable.i_1, 1))
        cardList.add(CardData(R.drawable.i_2, 2))
        cardList.add(CardData(R.drawable.i_3, 3))
        cardList.add(CardData(R.drawable.i_4, 4))
        cardList.add(CardData(R.drawable.i_5, 5))
        cardList.add(CardData(R.drawable.i_6, 6))
        cardList.add(CardData(R.drawable.i_7, 7))
        cardList.add(CardData(R.drawable.i_8, 8))
        cardList.add(CardData(R.drawable.i_9, 9))
        cardList.add(CardData(R.drawable.i_10, 10))


        imgsList.add(R.drawable.j_1)
        imgsList.add(R.drawable.j2)
        imgsList.add(R.drawable.j4)
        imgsList.add(R.drawable.j5)
        imgsList.add(R.drawable.j6)
        imgsList.add(R.drawable.j_7)
        imgsList.add(R.drawable.bg_game)
    }

    fun getData(count: Int): List<CardData> {
        cardList.shuffle()
        val list = cardList.subList(0, count / 2)
        val resultList = arrayListOf<CardData>()
        resultList.addAll(list)
        resultList.addAll(list)
        resultList.shuffle()
        return resultList
    }
    fun getImageForBg():Int{
        val k = Random.nextInt(0,7)
        return imgsList[k]
    }


}