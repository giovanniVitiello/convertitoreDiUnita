package com.example.convertitorediunita

//https://www.reddit.com/r/Kotlin/comments/bzsiw3/force_exhaustive_whenstatement_sealed_classes/

val <T> T.exhaustive: T
    get() = this
