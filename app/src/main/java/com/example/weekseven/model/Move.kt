package com.example.weekseven.model

import com.example.weekseven.model.MoveX
import com.example.weekseven.model.VersionGroupDetail

data class Move(
        val move: MoveX,
        val version_group_details: List<VersionGroupDetail>
)