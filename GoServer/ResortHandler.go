package main

import (
	"A4/Model"
	"encoding/json"
	"log"
	"net/http"
	"regexp"
	"strconv"
	"strings"
)

func ResortHandler(w http.ResponseWriter, r *http.Request) {
	path := r.RequestURI
	//log.Println(path)
	if !isUrlValidPt1(path) && !isUrlValidPt2(path) {
		http.NotFound(w, r)
		return
	}
	if r.Method == "GET" {
		//_, _ = fmt.Fprintln(w, "this is a get")
		doResortGet(w, r)
	} else if r.Method == "POST" {
		doResortPost(w, r)
	}
	//_, err := fmt.Fprint(w, "this is resorts!")
	//if err != nil {
	//	w.WriteHeader(http.StatusInternalServerError)
	//}
}

func doResortGet(w http.ResponseWriter, r *http.Request) {
	log.Print(db.Stats())
	w.Header().Set("Content-Type", "application/json")
	path := r.RequestURI
	if isUrlValidPt1(path) {
		w.WriteHeader(200)
		resortsList := GetAllResorts()
		js, _ := json.Marshal(resortsList)
		_, _ = w.Write(js)
	} else if isUrlValidPt2(path) {
		idString := strings.Split(path, "/")[2]
		resortId, _ := strconv.Atoi(idString)
		if GetResortByResortId(resortId)==nil{
			w.WriteHeader(404)
			_, _ = w.Write([]byte("message: resortId not found"))
		}else{
			w.WriteHeader(200)
			seasonsList:= GetSeasonByResortId(resortId)
			js,_:=json.Marshal(seasonsList)
			_, _ = w.Write(js)
		}
	}else{
		w.WriteHeader(404)
		_, _ =w.Write([]byte("message: not found"))
	}
}

func doResortPost(w http.ResponseWriter, r *http.Request) {
	decoder:=json.NewDecoder(r.Body)
	season:=Model.Season{-1}
	err:=decoder.Decode(&season)
	if err!=nil{
		w.WriteHeader(400)
		_, _ = w.Write([]byte("invalid input"))
		return
	}
	log.Println(season.Year)
	path:= r.RequestURI
	if isUrlValidPt2(path){
		idString:= strings.Split(path, "/")[2]
		resortId,_:=strconv.Atoi(idString)
		if GetResortByResortId(resortId)==nil{
			w.WriteHeader(404)
			_, _ = w.Write([]byte("message: resortId not found"))
		}else {
			w.WriteHeader(200)
			CreateSeason(season.Year, resortId)
			_, _ = w.Write([]byte("message: created new Season"))
		}
	}else{
		w.WriteHeader(404)
		_, _ = w.Write([]byte("not found"))
	}
}

func isUrlValidPt1(url string) bool {
	return strings.Compare(url, "/resorts/") == 0
}

func isUrlValidPt2(url string) bool {
	re := regexp.MustCompile(`/resorts/(\d+)/seasons`)
	return re.Match([]byte(url))
}
