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

func SkierHandler(w http.ResponseWriter, r *http.Request) {
	path := r.RequestURI
	if len(r.URL.Query()) == 0 {
		if isUrlValidNoQuery(path) {
			if r.Method == "GET" {
				doGetBySkier(w, r)
			} else if (r.Method == "POST") {
				doSkierPost(w, r)
			}
		} else {
			http.NotFound(w, r)
			return
		}
	} else {
		if isUrlValidQuery(path) {
			doGetTotal(w, r)
		} else {
			http.NotFound(w, r)
			return
		}
	}
}

func doGetBySkier(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	path := r.RequestURI
	params := strings.Split(path, "/")
	resortId, _ := strconv.Atoi(params[2])
	seasonId, _ := strconv.Atoi(params[4])
	dayId, _ := strconv.Atoi(params[6])
	skierId, _ := strconv.Atoi(params[8])
	vertical := GetVerticalBySkier(resortId, seasonId, dayId, skierId)
	res := strconv.Itoa(vertical)
	_, _ = w.Write([]byte(res))
}

func doGetTotal(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	resortKeys, ok := r.URL.Query()["resort"]
	var resortId int
	if !ok || len(resortKeys) == 0 {
		w.WriteHeader(400)
		_, _ = w.Write([]byte("wrong query format"))
		return
	}
	resortId, _ = strconv.Atoi(resortKeys[0])
	seasonKeys, ok := r.URL.Query()["season"]
	var seasonId int
	if !ok || len(seasonKeys) == 0 {
		seasonId = -1
	} else {
		seasonId, _ = strconv.Atoi(seasonKeys[0])
	}
	skierId, _ := strconv.Atoi(strings.Split(r.RequestURI, "/")[2])
	w.WriteHeader(200)
	skierVertical := GetTotalVertical(skierId, resortId, seasonId)
	js, _ := json.Marshal(skierVertical)
	_, _ = w.Write(js)
}

func doSkierPost(w http.ResponseWriter, r *http.Request) {
	if r.Body == nil {
		http.Error(w, "Please send a request body", 400)
		return
	}
	var lr Model.LiftRide
	err := json.NewDecoder(r.Body).Decode(&lr)
	if err != nil {
		http.Error(w, err.Error(), 400)
		return
	}
	w.Header().Set("Content-Type", "application/json")
	path := r.RequestURI
	params := strings.Split(path, "/")
	resortId, _ := strconv.Atoi(params[2])
	seasonId, _ := strconv.Atoi(params[4])
	dayId, _ := strconv.Atoi(params[6])
	skierId, _ := strconv.Atoi(params[8])
	InsertLiftRide(resortId, seasonId, dayId, skierId, &lr)
	w.WriteHeader(200)
	_, _ = w.Write([]byte("ok"))
}

func isUrlValidNoQuery(path string) bool {
	re1 := regexp.MustCompile(`/skiers/(\d+)/seasons/(\d+)/days/(\d+)/skiers/(\d+)`)
	b1 := re1.Match([]byte(path))
	if !b1 {
		return false
	}
	if b1 {
		day, err := strconv.Atoi(strings.Split(path, "/")[6])
		if err != nil || day > 366 || day < 1 {
			log.Panic(err)
			return false
		}
	}
	return true
}

func isUrlValidQuery(path string) bool {
	re2 := regexp.MustCompile(`/skiers/(\d+)/vertical\?resort=\d+(&season=\d+)?`)
	b2 := re2.Match([]byte(path))

	return b2
}
