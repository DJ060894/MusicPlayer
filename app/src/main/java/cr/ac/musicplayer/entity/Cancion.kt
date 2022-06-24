package cr.ac.musicplayer.entity

class Cancion {
    var nombre = String
    var author = String
    var album = String

    constructor()
    constructor(nombre: String, author: String, album: String) {
        this.nombre
        this.author
        this.album
    }

    override fun toString(): String {
        return "Cancion(nombre=$nombre, author=$author, album=$album)"
    }

}