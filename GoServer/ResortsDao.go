package main

import (
	"A4/Model"
	"log"
)

type ResortsDao struct {
}

func GetAllResorts() *Model.ResortsList {
	list := make([]Model.ResortsListResorts, 0, 0)
	selectResorts := "SELECT * FROM Resorts"
	rows, err := db.Query(selectResorts)
	if err != nil {
		log.Panic(err)
	}
	var ResortName string
	var ResortId int
	defer rows.Close()

	for rows.Next() {
		err := rows.Scan(&ResortId, &ResortName)
		if err != nil {
			log.Panic(err)
		}
		resort := Model.ResortsListResorts{ResortID: ResortId, ResortName: ResortName}
		list = append(list, resort)
	}
	resorts := Model.ResortsList{list}
	return &resorts
}

func GetResortByResortId(resortId int) *Model.ResortsListResorts {
	var resort Model.ResortsListResorts
	selectSeasons := "SELECT * FROM Resorts WHERE ResortId=?;"
	row := db.QueryRow(selectSeasons, resortId)
	var ResortId int
	var ResortName string

	if row!=nil{
		err := row.Scan(&ResortId, &ResortName)
		if err != nil {
			log.Panic(err)
		}
		resort = Model.ResortsListResorts{ResortName: ResortName, ResortID: ResortId}
	}else{
		return nil
	}

	return &resort
}

func GetSeasonByResortId(resortId int) *Model.SeasonList {
	list := make([]Model.Season, 0, 0)
	selectSeasons :=
		"SELECT Season " +
			"FROM Resorts INNER JOIN Seasons " +
			" ON Resorts.ResortId = Seasons.ResortId " +
			"WHERE Resorts.ResortId=?;"

	rows, err := db.Query(selectSeasons, resortId)
	if err != nil {
		log.Panic(err)
	}
	var Season int
	defer rows.Close()

	for rows.Next() {
		err := rows.Scan(&Season)
		if err != nil {
			log.Panic(err)
		}
		season := Model.Season{Season}
		list = append(list, season)
	}
	seasons := Model.SeasonList{list}
	return &seasons
}

func CreateSeason(season int, resortId int) {
	insertSeason := "INSERT INTO Seasons(Season, ResortId) VALUES(?,?);"
	stmt, err:=db.Prepare(insertSeason)
	if err!=nil{
		log.Panic(err)
	}
	_,err= stmt.Exec(season, resortId)
	stmt.Close()
	if err!=nil{
		log.Panic(err)
	}
}
