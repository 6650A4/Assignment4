package Model

type SeasonList struct{
	Seasons []Season
}

func NewSeasonList(seasons []Season) *SeasonList {
	return &SeasonList{Seasons: seasons}
}

func addSeason(seasons *SeasonList, season Season) *SeasonList{
	seasons.Seasons = append(seasons.Seasons, season)
	return seasons
}