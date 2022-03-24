package dev.w1zzrd.btpw.service

interface ContentStore {
    suspend fun verifyCredentials(username: String, password: String): Boolean
}