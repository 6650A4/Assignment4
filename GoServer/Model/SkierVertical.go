package Model

type SkierVertical struct{
	Resorts []SkierVerticalResort
}

func NewSkierVertical(resorts []SkierVerticalResort) *SkierVertical {
	return &SkierVertical{Resorts: resorts}
}


func AddSkierVerticalResort(list *SkierVertical, resort SkierVerticalResort) *SkierVertical{
	list.Resorts = append(list.Resorts, resort);
	return list
}