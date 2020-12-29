package com.bubba

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import ktx.assets.getAsset
import ktx.assets.load

enum class SoundAssets(val path: String) {
    Drop("drop.wav")
}

enum class MusicAssets(val path: String) {
    Rain("rain.mp3")
}

enum class TextureAssets(val path: String) {
    Bucket("bucket.png"),
    Drop("drop.png")
}

fun AssetManager.load(asset: SoundAssets) = load<Sound>(asset.path)
fun AssetManager.load(asset: MusicAssets) = load<Music>(asset.path)
fun AssetManager.load(asset: TextureAssets) = load<Texture>(asset.path)

fun AssetManager.get(asset: SoundAssets) = getAsset<Sound>(asset.path)
fun AssetManager.get(asset: MusicAssets) = getAsset<Music>(asset.path)
fun AssetManager.get(asset: TextureAssets) = getAsset<Texture>(asset.path)


