package com.shahar91.foodwatcher.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import be.appwise.room.BaseRoomDao
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.data.models.FoodEntryEntity
import com.shahar91.foodwatcher.utils.HawkManager
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FoodEntryDao : BaseRoomDao<FoodEntryEntity>(DBConstants.FOOD_ENTRY_TABLE_NAME) {

    @Transaction
    @Query("SELECT * FROM ${DBConstants.FOOD_ENTRY_TABLE_NAME} WHERE date BETWEEN :fromDate AND :toDate ORDER BY meal")
    abstract fun getFoodEntriesFlow(fromDate: Long, toDate: Long): Flow<List<FoodEntry>>

    @Transaction
    @Query("SELECT * FROM ${DBConstants.FOOD_ENTRY_TABLE_NAME} WHERE date BETWEEN :fromDate AND :toDate ORDER BY meal")
    abstract fun getFoodEntries(fromDate: Long, toDate: Long): LiveData<List<FoodEntry>>

    @Query("SELECT * FROM ${DBConstants.FOOD_ENTRY_TABLE_NAME} WHERE id == :foodEntryId")
    abstract suspend fun findEntryById(foodEntryId: String): FoodEntry?

//    @Query("SELECT date, SUM(foodItemPoints * amount) as dayTotal FROM foodEntry WHERE date BETWEEN :startWeek AND :endWeek GROUP BY date")
//    fun getWeekTotal(startWeek: Long, endWeek: Long): LiveData<List<DayTotal>>

    @Query("SELECT SUM(foodItemPoints * amount) as dayTotal FROM foodEntry WHERE date BETWEEN :fromDate AND :toDate")
    abstract fun getDayTotal(fromDate: Long, toDate: Long): Int

    @Transaction
    // TODO: This could be made a lot better, this is really buggy code...
    open suspend fun getWeekTotal(startWeek: Long): Float {
        val add1Day = (60 * 60 * 24 * 1000L)

        val mondayEndMillis = startWeek + add1Day
        val tuesdayEndMillis = mondayEndMillis + add1Day
        val wednesdayEndMillis = tuesdayEndMillis + add1Day
        val thursdayEndMillis = wednesdayEndMillis + add1Day
        val fridayEndMillis = thursdayEndMillis + add1Day
        val saturdayEndMillis = fridayEndMillis + add1Day
        val sundayEndMillis = saturdayEndMillis + add1Day

        val monday = getDayTotal(startWeek, mondayEndMillis - 1000L)
        val tuesday = getDayTotal(mondayEndMillis, tuesdayEndMillis - 1000L)
        val wednesday = getDayTotal(tuesdayEndMillis, wednesdayEndMillis - 1000L)
        val thursday = getDayTotal(wednesdayEndMillis, thursdayEndMillis - 1000L)
        val friday = getDayTotal(thursdayEndMillis, fridayEndMillis - 1000L)
        val saturday = getDayTotal(fridayEndMillis, saturdayEndMillis - 1000L)
        val sunday = getDayTotal(saturdayEndMillis, sundayEndMillis - 1000L)

        val dayTotal = HawkManager.hawkMaxDayTotal
        var weekTotal: Float = HawkManager.hawkMaxWeekTotal.toFloat()
        listOf(monday, tuesday, wednesday, thursday, friday, saturday, sunday).forEach {
            if (it > dayTotal) {
                weekTotal -= (it - dayTotal)
            }
        }

        return weekTotal
    }
}