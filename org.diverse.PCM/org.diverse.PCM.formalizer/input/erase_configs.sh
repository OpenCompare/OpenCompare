#!/bin/sh
for file in *.config; do
	rm $file
	touch $file    
done
