package com.shahar91.foodwatcher.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.shahar91.foodwatcher.BuildConfig
import com.shahar91.foodwatcher.data.firebaseLiveData.livedata
import com.shahar91.foodwatcher.data.models.DayDescription
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.transformLatest


class DayDescriptionRepositoryImpl : DayDescriptionRepository {

    companion object {
        private const val COLLECTION_DAY_DESCRIPTION = "dayDescriptions"
        private const val ENVIRONMENT = BuildConfig.FLAVOR_env
    }

    private val completeList = FirebaseFirestore.getInstance().collection(ENVIRONMENT).document(ENVIRONMENT).collection(COLLECTION_DAY_DESCRIPTION).livedata(DayDescription::class.java)

    override suspend fun createDayDescription(dayDescription: DayDescription): Long {
        FirebaseFirestore.getInstance().collection(ENVIRONMENT).document(ENVIRONMENT).collection(COLLECTION_DAY_DESCRIPTION).document(dayDescription.id).set(dayDescription)
        return -1
    }

    override fun getDescriptionForDay(fromDate: Long, toDate: Long): LiveData<DayDescription?> =
        completeList.asFlow()
            .map {
                it.firstOrNull { item -> item.date in fromDate..toDate }
            }
            .asLiveData()

    override suspend fun deleteDayDescription(dayDescription: DayDescription) {
        FirebaseFirestore.getInstance().collection(ENVIRONMENT).document(ENVIRONMENT).collection(COLLECTION_DAY_DESCRIPTION).document(dayDescription.id).delete()
    }
}