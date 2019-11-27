package main

import (
	"A4/Model"
	"database/sql"
	"fmt"
	"github.com/go-sql-driver/mysql"
	"log"
	"strconv"
)

type LiftRidesDao struct {
}

func GetTotalVertical(skierId int, resortId int, seasonId int) *Model.SkierVertical {

	list := make([]Model.SkierVerticalResort, 0, 10)
	whereStmt := "WHERE ResortId=? AND SkierId=?"
	if seasonId > 0 {
		whereStmt = "WHERE ResortId=? AND SkierId=? AND SeasonId=?"
	}
	selectVertical := "SELECT SeasonId, SUM(Vertical) AS TotalVertical " +
		"FROM (SELECT SeasonId, Vertical FROM LiftRides " +
		whereStmt +
		") AS V " +
		"GROUP BY SeasonId;"
	var rows *sql.Rows
	var err error
	if seasonId > 0 {
		rows, err = db.Query(selectVertical, resortId, skierId, seasonId)
	} else {
		rows, err = db.Query(selectVertical, resortId, skierId)
	}
	if err != nil {
		panic(err.Error())
	}
	var TotalVertical int
	var SeasonId int
	for rows.Next() {
		err := rows.Scan(&SeasonId, &TotalVertical)
		if err != nil {
			log.Panic(err)
		}
		fmt.Println(strconv.FormatInt(int64(SeasonId), 10))
		fmt.Println(TotalVertical)
		resort := Model.SkierVerticalResort{SeasonID: strconv.FormatInt(int64(SeasonId), 10), TotalVert: TotalVertical}
		list = append(list, resort)
	}
	rows.Close()

	total := Model.NewSkierVertical(list)
	return total
}

func GetVerticalBySkier(resortId int, seasonId int, dayId int, skierId int) int {
	TotalVertical := 0
	selectVertical := "SELECT SUM(Vertical) AS TotalVertical " +
		"FROM (SELECT Vertical FROM LiftRides " +
		"WHERE ResortId=? AND SeasonId=? AND DayId=? AND SkierId=?) AS V;"
	row:= db.QueryRow(selectVertical, resortId, seasonId, dayId, skierId)

	if row!=nil {
		err := row.Scan(&TotalVertical)
		if err != nil {
			log.Panic(err)
		}
	}

	return TotalVertical
}

func InsertLiftRide(resortId int, seasonId int, dayId int, skierId int, liftRide *Model.LiftRide) {
	insertLiftRide := "INSERT INTO LiftRides(ResortId, SeasonId, DayId, SkierId, StartTime, LiftId, Vertical) VALUES(%d,%d,%d,%d,%d,%d,%d);"
	ss:=fmt.Sprintf(insertLiftRide, resortId, seasonId, dayId, skierId, liftRide.Time, liftRide.LiftID, liftRide.LiftID*10)
	_, err = db.Exec(ss)
	if mysqlError, ok := err.(*mysql.MySQLError); ok {
		if mysqlError.Number != 1062 {
			log.Panic(err)
		}
	}
}
