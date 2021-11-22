package com.shahar91.foodwatcher

import androidx.lifecycle.LiveData
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query

fun <T> CollectionReference.livedata(clazz: Class<T>): LiveData<List<T>> {
    return CollectionLiveDataNative(this, clazz)
}

fun <T> CollectionReference.livedata(parser: (documentSnapshot: DocumentSnapshot) -> T): LiveData<List<T>> {
    return CollectionLiveDataCustom(this, parser)
}

fun CollectionReference.livedata(): LiveData<QuerySnapshot> {
    return CollectionLiveDataRaw(this)
}

private class CollectionLiveDataNative<T>(private val collectionReference: CollectionReference,
                                          private val clazz: Class<T>) : LiveData<List<T>>() {

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()

        listener = collectionReference.addSnapshotListener { querySnapshot, exception ->
            if (exception == null) {
                value = querySnapshot?.documents?.map { it.toObject(clazz)!! }
            } else {
                Log.e("FireStoreLiveData", "", exception)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()

        listener?.remove()
        listener = null
    }
}

private class CollectionLiveDataCustom<T>(private val collectionReference: CollectionReference,
                                          private val parser: (documentSnapshot: DocumentSnapshot) -> T) : LiveData<List<T>>() {

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()

        listener = collectionReference.addSnapshotListener { querySnapshot, exception ->
            if (exception == null) {
                value = querySnapshot?.documents?.map { parser.invoke(it) }
            } else {
                Log.e("FireStoreLiveData", "", exception)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()

        listener?.remove()
        listener = null
    }
}

private class CollectionLiveDataRaw(private val collectionReference: CollectionReference) : LiveData<QuerySnapshot>() {

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()

        listener = collectionReference.addSnapshotListener { querySnapshot, exception ->
            if (exception == null) {
                value = querySnapshot
            } else {
                Log.e("FireStoreLiveData", "", exception)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()

        listener?.remove()
        listener = null
    }
}

fun <T> DocumentReference.livedata(clazz: Class<T>): LiveData<T> {
    return DocumentLiveDataNative(this, clazz)
}

fun <T> DocumentReference.livedata(parser: (documentSnapshot: DocumentSnapshot) -> T): LiveData<T> {
    return DocumentLiveDataCustom(this, parser)
}

fun DocumentReference.livedata(): LiveData<DocumentSnapshot> {
    return DocumentLiveDataRaw(this)
}

private class DocumentLiveDataNative<T>(private val documentReference: DocumentReference,
                                        private val clazz: Class<T>) : LiveData<T>() {

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()

        listener = documentReference.addSnapshotListener { documentSnapshot, exception ->
            if (exception == null) {
                documentSnapshot?.let {
                    value = it.toObject(clazz)
                }
            } else {
                Log.e("FireStoreLiveData", "", exception)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()

        listener?.remove()
        listener = null
    }
}

class DocumentLiveDataCustom<T>(private val documentReference: DocumentReference,
                                private val parser: (documentSnapshot: DocumentSnapshot) -> T) : LiveData<T>() {

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()

        listener = documentReference.addSnapshotListener { documentSnapshot, exception ->
            if (exception == null) {
                documentSnapshot?.let {
                    value = parser.invoke(it)
                }
            } else {
                Log.e("FireStoreLiveData", "", exception)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()

        listener?.remove()
        listener = null
    }
}

class DocumentLiveDataRaw(private val documentReference: DocumentReference) : LiveData<DocumentSnapshot>() {

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()

        listener = documentReference.addSnapshotListener { documentSnapshot, exception ->
            if (exception == null) {
                value = documentSnapshot
            } else {
                Log.e("FireStoreLiveData", "", exception)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()

        listener?.remove()
        listener = null
    }
}

fun <T> Query.livedata(clazz: Class<T>): LiveData<List<T>> {
    return QueryLiveDataNative(this, clazz)
}

fun <T> Query.livedata(parser: (documentSnapshot: DocumentSnapshot) -> T): LiveData<List<T>> {
    return QueryLiveDataCustom(this, parser)
}

fun Query.livedata(): LiveData<QuerySnapshot> {
    return QueryLiveDataRaw(this)
}

private class QueryLiveDataNative<T>(private val query: Query,
                                     private val clazz: Class<T>) : LiveData<List<T>>() {

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()

        listener = query.addSnapshotListener { querySnapshot, exception ->
            if (exception == null) {
                value = querySnapshot?.documents?.map { it.toObject(clazz)!! }
            } else {
                Log.e("FireStoreLiveData", "", exception)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()

        listener?.remove()
        listener = null
    }
}

private class QueryLiveDataCustom<T>(private val query: Query,
                                     private val parser: (documentSnapshot: DocumentSnapshot) -> T) : LiveData<List<T>>() {

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()

        listener = query.addSnapshotListener { querySnapshot, exception ->
            if (exception == null) {
                value = querySnapshot?.documents?.map { parser.invoke(it) }
            } else {
                Log.e("FireStoreLiveData", "", exception)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()

        listener?.remove()
        listener = null
    }
}

private class QueryLiveDataRaw(private val query: Query) : LiveData<QuerySnapshot>() {

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()

        listener = query.addSnapshotListener { querySnapshot, exception ->
            if (exception == null) {
                value = querySnapshot
            } else {
                Log.e("FireStoreLiveData", "", exception)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()

        listener?.remove()
        listener = null
    }
}