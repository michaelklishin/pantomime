## Changes between Pantomime 1.5.0 and 1.6.0

### Clojure 1.5 By Default

Pantomime now depends on `org.clojure/clojure` version `1.5.0`. It is
still compatible with Clojure 1.3+ and if your `project.clj` depends
on a different version, it will be used, but 1.5 is the default now.

We encourage all users to upgrade to 1.5, it is a drop-in replacement
for the majority of projects out there.


## Changes between Pantomime 1.4.0 and 1.5.0

### Apache Tika 1.3

Apache Tika dependency has been upgraded to [version 1.3](http://www.apache.org/dist/tika/CHANGES-1.3.txt).

### Clojure 1.4 By Default

Pantomime now depends on `org.clojure/clojure` version `1.4.0`. It is still compatible with Clojure 1.3 and if your `project.clj` depends
on 1.3, it will be used, but 1.4 is the default now.

We encourage all users to upgrade to 1.4, it is a drop-in replacement for the majority of projects out there.


## Changes between Pantomime 1.3.0 and 1.4.0

### Apache Tika 1.2

Apache Tika dependency has been upgraded to [version 1.2](http://www.apache.org/dist/tika/CHANGES-1.2.txt).


## Changes between Pantomime 1.3.0-beta2 and 1.3.0

### pantomime.web

Contains the same functions as pantomime.mime but is Web-oriented. Apache Tika as of April 2012 cannot
detect PNG, JPEG and other image bytes for byte arrays. However, it is not uncommon to see broken Web
frameworks, apps and servers that serve, say, PDF files claiming that they are text/html. pantomime.web
attempts to improve the situation by providing special MIME type detection functions that can use
content-based detection and Content-Type header at the same time.

New functions:

* `pantomime.web/mime-type-of`


### New functions in pantomime.media.MediaTypeOps

New functions that return media type parameters and charset:

* `pantomime.media/has-parameters?`


## Changes between Pantomime 1.3.0-beta1 and 1.3.0-beta2

### New functions in pantomime.media.MediaTypeOps

New functions that return media type parameters and charset:

* `pantomime.media/parameters-of`
* `pantomime.media/charset-of`


## Changes between Pantomime 1.2.0 and 1.3.0-beta1

### New predicate functions in pantomime.media.MediaTypeOps

New media type functions:

* `pantomime.media/parse`
* `pantomime.media/application?`
* `pantomime.media/text?`
* `pantomime.media/audio?`
* `pantomime.media/image?`
* `pantomime.media/video?`
* `pantomime.media/multipart?`


### pantomime.media.BaseMediaType protocol renamed

pantomime.media.BaseMediaType protocol is now pantomime.media.MediaTypeOps. Function names haven't changed
so for code that does not extend or reify this protocol, this is a backwards-compatible change.


### Apache Tika 1.1

Apache Tika dependency has been upgraded to version 1.1.



## Changes between Pantomime 1.1.0 and 1.2.0

### pantomime.mime/MIMETypeDetection support for byte arrays

pantomime.mime/MIMETypeDetection now supports byte arrays


### Leiningen 2

Pantomime now uses [Leiningen 2](https://github.com/technomancy/leiningen/wiki/Upgrading).
