package com.medcare.pillreminder.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataStore(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("medcare_data", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveMedications(list: List<Medication>) {
        prefs.edit().putString("medications", gson.toJson(list)).apply()
    }

    fun loadMedications(): MutableList<Medication> {
        val json = prefs.getString("medications", null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Medication>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveHistory(history: MutableMap<String, MutableMap<String, Boolean>>) {
        prefs.edit().putString("history", gson.toJson(history)).apply()
    }

    fun loadHistory(): MutableMap<String, MutableMap<String, Boolean>> {
        val json = prefs.getString("history", null) ?: return mutableMapOf()
        val type = object : TypeToken<MutableMap<String, MutableMap<String, Boolean>>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveChecksDate(date: String) {
        prefs.edit().putString("checksData", date).apply()
    }

    fun loadChecksDate(): String {
        return prefs.getString("checksData", "") ?: ""
    }

    fun saveContacts(contacts: List<Any>) {
        prefs.edit().putString("contacts", gson.toJson(contacts)).apply()
    }

    fun loadContacts(): MutableList<Any> {
        val json = prefs.getString("contacts", null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Any>>() {}.type
        return gson.fromJson(json, type)
    }
}
