package com.shahar91.foodwatcher.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.shahar91.foodwatcher.BuildConfig
import com.shahar91.foodwatcher.data.firebaseLiveData.livedata
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.utils.HawkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FoodEntryRepositoryImpl : FoodEntryRepository {

    companion object {
        private const val COLLECTION_FOOD_ENTRY = "foodEntries"
        private const val ENVIRONMENT = BuildConfig.FLAVOR_env
    }

    private val completeList = FirebaseFirestore.getInstance().collection(ENVIRONMENT).document(ENVIRONMENT).collection(COLLECTION_FOOD_ENTRY).livedata(FoodEntry::class.java)

    override fun getFoodEntriesFlow(fromDate: Long, toDate: Long): Flow<List<FoodEntry>> =
        completeList.asFlow().map { it.filter { item -> item.date in fromDate..toDate }.sortedBy { item -> item.meal } }

    override fun getFoodEntries(fromDate: Long, toDate: Long): LiveData<List<FoodEntry>> =
        completeList.asFlow()
            .map {
                it.filter { item -> item.date in fromDate..toDate }.sortedBy { item -> item.meal }
            }
            .asLiveData()

    override suspend fun createFoodEntry(foodEntry: FoodEntry): Long {
        FirebaseFirestore.getInstance().collection(ENVIRONMENT).document(ENVIRONMENT).collection(COLLECTION_FOOD_ENTRY).document(foodEntry.id).set(foodEntry)
        return -1
    }

    override suspend fun deleteFoodEntry(foodEntry: FoodEntry) {
        FirebaseFirestore.getInstance().collection(ENVIRONMENT).document(ENVIRONMENT).collection(COLLECTION_FOOD_ENTRY).document(foodEntry.id).delete()
    }

    override suspend fun getWeekTotal(startWeek: Long): Float {
        val add1Day = (60 * 60 * 24 * 1000L)

        val mondayEndMillis = startWeek + add1Day
        val tuesdayEndMillis = mondayEndMillis + add1Day
        val wednesdayEndMillis = tuesdayEndMillis + add1Day
        val thursdayEndMillis = wednesdayEndMillis + add1Day
        val fridayEndMillis = thursdayEndMillis + add1Day
        val saturdayEndMillis = fridayEndMillis + add1Day
        val sundayEndMillis = saturdayEndMillis + add1Day

        val list = completeList.value
        val monday = getDayTotal(list, startWeek, mondayEndMillis - 1000L)
        val tuesday = getDayTotal(list, mondayEndMillis, tuesdayEndMillis - 1000L)
        val wednesday = getDayTotal(list, tuesdayEndMillis, wednesdayEndMillis - 1000L)
        val thursday = getDayTotal(list, wednesdayEndMillis, thursdayEndMillis - 1000L)
        val friday = getDayTotal(list, thursdayEndMillis, fridayEndMillis - 1000L)
        val saturday = getDayTotal(list, fridayEndMillis, saturdayEndMillis - 1000L)
        val sunday = getDayTotal(list, saturdayEndMillis, sundayEndMillis - 1000L)

        val dayTotal = HawkManager.hawkMaxDayTotal
        var weekTotal: Float = HawkManager.hawkMaxWeekTotal.toFloat()
        listOf(monday, tuesday, wednesday, thursday, friday, saturday, sunday).forEach {
            if (it > dayTotal) {
                weekTotal -= (it - dayTotal)
            }
        }

        return weekTotal
    }

    private fun getDayTotal(list: List<FoodEntry>?, fromDate: Long, toDate: Long): Int {
        if (list == null) return 0

        return list
            .filter { item -> item.date in fromDate..toDate }
            .sumOf { it.foodItemPoints.toDouble() * it.amount }.toInt()
    }


    override suspend fun findFoodEntryById(foodEntryId: String): FoodEntry? = suspendCancellableCoroutine { c ->
        FirebaseFirestore.getInstance().collection(ENVIRONMENT).document(ENVIRONMENT).collection(COLLECTION_FOOD_ENTRY).document(foodEntryId).get().addOnCompleteListener {
            if (!c.isActive) return@addOnCompleteListener

            if (it.isSuccessful) {
                c.resume(it.result?.toObject(FoodEntry::class.java))
            } else {
                c.resume(null)
            }
        }
    }

    override suspend fun updateFoodEntry(foodEntry: FoodEntry) {
        FirebaseFirestore.getInstance().collection(ENVIRONMENT).document(ENVIRONMENT).collection(COLLECTION_FOOD_ENTRY).document(foodEntry.id).set(foodEntry)
    }
}