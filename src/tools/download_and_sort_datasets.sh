#! /bin/sh

run() {
  echo "Downloading and sorting $3"
  ./download_and_sort_dataset.sh $1 $2
}

#run fr/short_abstracts_fr.nt.bz2 short_abstracts_fr.snt.gz "FR abstracts"
#run en/mappingbased_properties_en.nt.bz2 props_en.snt.gz "EN ontology properties"
run fr/mappingbased_properties_fr.nt.bz2 props_fr.snt.gz "FR ontology properties"
