package com.example.rickandmorty

import com.example.rickandmorty.GetCharacterByIdResponse
import com.example.rickandmorty.RickAndMortyService
import retrofit2.Response

class ApiClient(
    private val rickAndMortyService: RickAndMortyService
) {

    suspend fun getCharacterById(characterId: Int): Response<GetCharacterByIdResponse> {
        return rickAndMortyService.getCharacterById(characterId)
    }
}