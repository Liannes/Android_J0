package com.example.dz1_empty_pj.service

interface GetService {
    fun getService(): ServerContact?

    fun getStatusBound(): Boolean
}