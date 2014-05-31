#abson
=====

Abson (short for abstract [jb]son) is a library for converting json to bson and vice versa; abson was
designed firstly with speed in mind (hence the notable usage of Writers, Readers, and an avoidance of
directly calling functions on immutable String and array objects).

Abson can parse most JSON, regardless of whitespacing or size (as very little buffering is done when parsing), and follows
conventions in both JSON and BSON where possible.

##Deviations and other things to note
=====

While abson for the most part adheres to JSON and BSON standards, there is currently **one thing** which is not found
in normal JSON which is used in Abson: the "binary literal." While text is not a good form of transferring binary data,
it is sometimes unavoidable, and hence Abson uses a Base64 encoder and decoder to represent binary data. A binary literal
is of the form '#"[Binary Data]"'.