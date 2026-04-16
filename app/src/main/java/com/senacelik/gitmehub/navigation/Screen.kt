package com.senacelik.gitmehub.navigation

import java.net.URLEncoder

sealed class Screen(val route: String) {
    object RepoList : Screen("repo_list")
    object Starred : Screen("starred_repos")
    object Detail : Screen("repo_detail/{url}") {
        fun passUrl(url: String) = "repo_detail/${URLEncoder.encode(url, "UTF-8")}"
    }
}