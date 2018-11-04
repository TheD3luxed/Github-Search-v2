package pl.karolmichalski.githubsearchv2.presentation.utils

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

sealed class BundleDelegate<T>(protected val key: kotlin.String) : ReadWriteProperty<Bundle, T> {

	class String(key: kotlin.String) : BundleDelegate<kotlin.String>(key) {
		override fun setValue(thisRef: Bundle, property: KProperty<*>, value: kotlin.String) {
			thisRef.putString(key, value)
		}
		override fun getValue(thisRef: Bundle, property: KProperty<*>): kotlin.String {
			return thisRef.getString(key, "")
		}
	}

//	class List<K: Parcelable>(key: kotlin.String): BundleDelegate<kotlin.collections.List<K>>(key){
//		override fun setValue(thisRef: Bundle, property: KProperty<*>, value: kotlin.collections.List<K>) {
//			thisRef.putParcelableArrayList(key, ArrayList(value))
//		}
//		override fun getValue(thisRef: Bundle, property: KProperty<*>): kotlin.collections.List<K> {
//			return thisRef.getParcelableArrayList(key) ?: ArrayList()
//		}
//	}
//
	class List<Any>(key: kotlin.String): BundleDelegate<kotlin.collections.List<kotlin.Any>>(key){
		override fun setValue(thisRef: Bundle, property: KProperty<*>, value: kotlin.collections.List<kotlin.Any>) {
			thisRef.putSerializable(key, value.toHashSet())
			TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
		}

		override fun getValue(thisRef: Bundle, property: KProperty<*>): kotlin.collections.List<kotlin.Any> {
			TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
		}
	}
}