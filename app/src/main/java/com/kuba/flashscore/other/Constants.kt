package com.kuba.flashscore.other

object Constants {
    const val BASE_URL = "https://apiv2.apifootball.com"

    const val DATABASE_NAME = "flash_score.db"
    const val ERROR_MESSAGE = "An unknown error occured"
    const val ERROR_INTERNET_CONNECTION_MESSAGE =
        "Couldn't reach the server. Check your internet connection"

    const val COUNTRIES = "Countries"
    const val TEAM_TAB = "team"
    const val CURRENT_SEASON_TAB = "current season"
    const val PLAYER_AGE = "Age:"
    const val PLAYER_NUMBER = "Number:"
    const val AWAY = "away"
    const val HOME = "home"
    const val OVERALL = "overall"
    const val GENERALLY_TAB = "generally"
    const val HOME_TAB = "home"
    const val AWAY_TAB = "away"
    const val TEAMS_TAB = "teams"
    const val TABLE_TAB = "table"
    const val RESULT_TAB = "result"
    const val MATCHES_TAB = "matches"

    const val EVENT_DERAILS_TAB = "Details"
    const val EVENT_STATISTICS_TAB = "Statistic"
    const val EVENT_TEAM_TAB = "Team"
    const val EVENT_HEAD_2_HEAD_TAB = "H2H"
    const val EVENT_TABLE_TAB = "Table"


    //StandingsAdapter
    const val OVERALL_LEAGUE = "overall"
    const val HOME_LEAGUE = "home"
    const val LEAGUE_PROMOTION_PREMIER_LEAGUE = "Promotion - Premier League"
    const val LEAGUE_PROMOTION_CHAMPIONSHIP_PLAY_OFFS = "Promotion - Championship (Play Offs)"
    const val LEAGUE_PROMOTION_LIGUE_1 = "Promotion - Ligue 1"
    const val LEAGUE_PROMOTION_LEAGUE_1_PROMOTION = "Promotion - Ligue 1 (Promotion)"
    const val LEAGUE_RELEGATION_LIGUE_2 = "Ligue 2 (Relegation)"
    const val LEAGUE_RELEGATION = "Relegation"


    //ApiFootballService
    const val API_KEY = "APIkey"
    const val COUNTRY_ID = "country_id"
    const val LEAGUE_ID = "league_id"
    const val TEAM_ID = "team_id"
    const val PLAYER_NAME = "player_name"
    const val PATH_GET_COUNTRIES = "/?action=get_countries"
    const val PATH_GET_LEAGUES = "/?action=get_leagues"
    const val PATH_GET_TEAMS = "/?action=get_teams"
    const val PATH_GET_STANDINGS = "/?action=get_standings"
    const val PATH_GET_PLAYERS = "/?action=get_players"
    const val PATH_GET_EVENTS = "/?action=get_events"
    const val PATH_FROM = "from"
    const val PATH_TO = "to"


    const val DATE_FORMAT_DAY_MONTH_YEAR = "dd.MM.yyyy"
    const val DATE_FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd"
    const val DATE_FORMAT_DAY_OF_WEEK = "EEEE"

    //Event Details
    const val ITEM_TYPE_NORMAL = 0
    const val ITEM_TYPE_HEADER = 1
    const val MATCH_STATUS_FINISHED = "Finished"
    const val INCIDENT_HEADER_2_HALF = "2. HALF"
    const val INCIDENT_HEADER_OVERTIME = "OVERTIME"
    const val SUBSTITUTION_DIVIDER = "|"
    const val CARD_YELLOW = "yellow card"
    const val RESULT_NULL_TO_NULL = "0 - 0"


}