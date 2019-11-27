package main

import (
	"fmt"
	"log"
	"net/http"
	"os"
)


func init(){
	dbConn()
}
func main() {
	http.HandleFunc("/", indexHandler)
	http.HandleFunc("/skiers/", SkierHandler)
	http.HandleFunc("/resorts/", ResortHandler)
	defer db.Close()
	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
		log.Printf("Defaulting to port %s", port)
	}

	//l:=DAO.GetAllResorts()
	//for i:=0; i< len(l.Resorts); i++ {
	//	fmt.Println(l.Resorts[i].ResortName)
	//}
	//r:=DAO.GetResortByResortId(1)
	//fmt.Println(r.ResortName)
	//DAO.CreateSeason(2011,1)
	//
	//s:=DAO.GetSeasonByResortId(1)
	//fmt.Println(s.Seasons[0].Year)
	//total:=DAO.GetTotalVertical(1,3,2000)
	//fmt.Println(total.Resorts[0].TotalVert)
	//aa:= Model.Season{1};
	//fmt.Println(aa.Year)
	//fmt.Println(DAO.GetVerticalBySkier(3,2000,2,1))
	//lr:=Model.LiftRide{1,2}
	//DAO.InsertLiftRide(1,1,1,1, &lr)
	//a := Model.ResortsListResorts{1, "a"};
	//b := Model.ResortsListResorts{2, "b"};
	//
	//ll := make([]Model.ResortsListResorts, 0, 0)
	//list:=Model.NewResortsList(ll)
	//list = Model.AddResort(list, a)
	//list = Model.AddResort(list, b)
	//fmt.Println(list.Resorts[0].ResortName);
	log.Printf("Listening on port %s", port)
	log.Printf("Open http://localhost:%s in the browser", port)
	log.Fatal(http.ListenAndServe(fmt.Sprintf(":%s", port), nil))

}

func indexHandler(w http.ResponseWriter, r *http.Request) {
	if r.URL.Path != "/" {
		http.NotFound(w, r)
		return
	}
	if r.Method == "GET" {
		_, _ = fmt.Fprintln(w, "this is a get")
	} else {
		_, _ = fmt.Fprintln(w, "this is not a get")
	}
	_, err := fmt.Fprint(w, "Hello, World!")
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
	}
}
