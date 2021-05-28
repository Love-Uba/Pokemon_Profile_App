package com.example.weekseven.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weekseven.model.*

/**
 * This viewmodel holds functions that first assigns the entire fetched data
 * to the live data store
 * **/

class PokeViewModel : ViewModel() {
    var pokeItemList = ArrayList<Result>()
    var pokeAbilities = ArrayList<AbilityX>()
    var pokeMoves = ArrayList<MoveX>()
    var pokeStatistics = ArrayList<Pair<String, String>>()
    var pokeType = ArrayList<TypeX>()

    var pokeListLive: MutableLiveData<MutableList<Result>> = MutableLiveData()

    fun addPokeToList(poke: Result) {
        pokeItemList.add(poke)
        pokeListLive.postValue(pokeItemList)
    }

    var pokeAbilityLive: MutableLiveData<MutableList<AbilityX>> = MutableLiveData()

    fun addAbility(abilityX: AbilityX) {
        pokeAbilities.add(abilityX)
        pokeAbilityLive.postValue(pokeAbilities)
    }

    var pokeMoveLive: MutableLiveData<MutableList<MoveX>> = MutableLiveData()

    fun addMove(moveX: MoveX) {
        pokeMoves.add(moveX)
        pokeMoveLive.postValue(pokeMoves)
    }

    var pokeStatLive: MutableLiveData<MutableList<Pair<String, String>>> = MutableLiveData()

    fun addStat(pair: Pair<String, String>) {
        pokeStatistics.add(pair)
        pokeStatLive.postValue(pokeStatistics)

    }

    var pokeTypeLive: MutableLiveData<MutableList<TypeX>> = MutableLiveData()

    fun addType(typeX: TypeX) {
        pokeType.add(typeX)
        pokeTypeLive.postValue(pokeType)
    }
}