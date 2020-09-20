all:
	true
	
watchdocs:
	docker run --rm -it -p 8000:8000 -v ${PWD}:/docs 7d890f99270e serve
#squidfunk/mkdocs-material

.PHONY: watchdocs all
