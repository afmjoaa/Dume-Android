package io.dume.dume.firstTimeUser

import io.dume.dume.poko.MiniUser

class NID(var name: String, var birth_date: String, var nid: Long)

class UserResponse(var state: UserState, var response: MiniUser? )
