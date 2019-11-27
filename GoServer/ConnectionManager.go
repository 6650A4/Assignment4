package main

import (
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
)

//func main() {
//	db := dbConn()
//	id:= 1
//	insForm, err := db.Prepare("INSERT INTO Game(AppID, GameName, Developer, Publisher) VALUES(?,?,?,?)")
//	if err != nil {
//		panic(err.Error())
//	}
//	_, err = insForm.Exec(id, "aaa","bbb","ccc")
//
//	defer db.Close()
//
//}

var db *sql.DB
var err error

func dbConn(){


	dbDriver := "mysql"
	//dbUser := "root"
	dbUser:="admin"
	dbPass:=""
	dbName := "SkiAPI"
	db, err = sql.Open(dbDriver, dbUser+":"+dbPass+"@tcp(database-1.cbbidv99xyac.us-east-1.rds.amazonaws.com:3306)/"+dbName)
	db.SetMaxIdleConns(100)
	db.SetMaxOpenConns(200)
	//db.SetConnMaxLifetime(12*time.Second)
	if err != nil {
		panic(err.Error())
	}
	//var (
	//	connectionName = "assignment4-260100:us-west2:myinstance2"
	//	user           = "root"
	//	dbName         = "SkiAPI" // NOTE: dbName may be empty
	//	password       = ""      // NOTE: password may be empty
	//	socket         = "/cloudsql"
	//)
	//
	//
	//
	////// MySQL Connection, comment out to use PostgreSQL.
	////// connection string format: USER:PASSWORD@unix(/cloudsql/PROJECT_ID:REGION_ID:INSTANCE_ID)/[DB_NAME]
	//dbURI := fmt.Sprintf("%s:%s@unix(%s/%s)/%s", user, password, socket, connectionName, dbName)
	//db, err = sql.Open("mysql", dbURI)
	//db.SetMaxIdleConns(100)
	//db.SetMaxOpenConns(200)
	//
	//// PostgreSQL Connection, uncomment to use.
	//// connection string format: user=USER password=PASSWORD host=/cloudsql/PROJECT_ID:REGION_ID:INSTANCE_ID/[ dbname=DB_NAME]
	//// dbURI := fmt.Sprintf("user=%s password=%s host=/cloudsql/%s dbname=%s", user, password, connectionName, dbName)
	//// conn, err := sql.Open("postgres", dbURI)
	//
	//if err != nil {
	//	//panic(fmt.Sprintf("DB: %v", err))
	//}
}
