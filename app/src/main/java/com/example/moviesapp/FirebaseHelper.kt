package com.example.moviesapp

import android.content.Context
import android.widget.Toast
import com.example.moviesapp.Entities.FavMovies
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper(private val context: Context) {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun AddFavoleriMovies(id: Int, onComplete: (Boolean) -> Unit){
        database.child("Users").child(auth.currentUser!!.uid).child("FavoriteFilms").push().setValue(FavMovies(id)).addOnCompleteListener { task->
            if(task.isSuccessful){
                Toast.makeText(context,"Added to favorites",Toast.LENGTH_SHORT).show()
                onComplete(true)
            }else{
                Toast.makeText(context,"Could not add to favorites",Toast.LENGTH_SHORT).show()
                onComplete(false)
            }
        }
    }

    fun removeFavoriteMovie(id: Int, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("Users").child(userId).child("FavoriteFilms").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    for (favSnapshot in snapshot.children) {
                        val favMovie = favSnapshot.getValue(FavMovies::class.java)
                        if (favMovie != null && favMovie.Film_id == id) {
                            favSnapshot.ref.removeValue().addOnCompleteListener { removeTask ->
                                if (removeTask.isSuccessful) {
                                    Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                                    onComplete(true)
                                } else {
                                    Toast.makeText(context, "Could not remove from favorites", Toast.LENGTH_SHORT).show()
                                    onComplete(false)
                                }
                            }
                            break
                        }
                    }
                } else {
                    Toast.makeText(context, "Error accessing favorites", Toast.LENGTH_SHORT).show()
                    onComplete(false)
                }
            }
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            onComplete(false)
        }
    }
    fun isFavoriteMovie(id: Int, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("Users").child(userId).child("FavoriteFilms").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    var found = false
                    for (favSnapshot in snapshot.children) {
                        val favMovie = favSnapshot.getValue(FavMovies::class.java)
                        if (favMovie != null && favMovie.Film_id == id) {
                            found = true
                            break
                        }
                    }
                    onComplete(found)
                } else {
                    Toast.makeText(context, "Error accessing favorites", Toast.LENGTH_SHORT).show()
                    onComplete(false)
                }
            }
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            onComplete(false)
        }
    }
}