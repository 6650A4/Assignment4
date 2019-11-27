package Model

type ResortsList struct {
	Resorts []ResortsListResorts
}

func NewResortsList(resorts []ResortsListResorts) *ResortsList {
	return &ResortsList{Resorts: resorts}
}

func AddResort(resortList *ResortsList, resort ResortsListResorts) *ResortsList{
	resortList.Resorts = append(resortList.Resorts, resort)
	return resortList
}
