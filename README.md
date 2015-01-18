# Pantomime, a Library For Working With MIME Types In Clojure

Pantomime is a tiny Clojure library that deals with MIME types
(Internet media types, sometimes referred to as "content types"). It
uses [Apache Tika](http://tika.apache.org/) under the hood to detect
MIME types using several techniques:

 * Known file extensions
 * Magic bytes
 * Content-type information for resources served via HTTP
 * XML schema information

and so on.


## Maven Artifacts

Pantomime artifacts are [released to Clojars](https://clojars.org/com.novemberain/pantomime). If you are
using Maven, add the following repository definition to your

`pom.xml`:

``` xml
<repository>
  <id>clojars.org</id>
  <url>http://clojars.org/repo</url>
</repository>
```


### Latest Release

With Leiningen:

```clojure
[com.novemberain/pantomime "2.4.0"]
```

With Maven:

    <dependency>
      <groupId>com.novemberain</groupId>
      <artifactId>pantomime</artifactId>
      <version>2.4.0</version>
    </dependency>


## Supported Clojure versions

Pantomime requires Clojure 1.6+. The most recent stable
release is highly recommended.


## Usage

### Detecting MIME type

`pantomime.mime/mime-type-of` function accepts content as byte arrays, java.io.InputStream and java.net.URL instances as well as
filenames as strings and java.io.File instances, and returns MIME type as a string or "application/octet-stream" if detection failed.

An example:

``` clojure
(ns your.app.namespace
  (:require [pantomime.mime :refer [mime-type-of]]))

;; by content (as byte array)
(mime-type-of (.getBytes "filename.pdf"))
;; by file extension
(mime-type-of "filename.pdf")
;; by file content (as java.io.File)
(mime-type-of (File. "some/file/without/extension"))
;; by content (as java.net.URL)
(mime-type-of (URL. "http://domain.com/some/url/path.pdf"))
```

Pantomime has a variation of `mime-type-of` function that is suitable for cases when content was fetched from the Web and
HTTP headers are also available:

``` clojure
(ns your.app.namespace
  (:require [pantomime.web :refer [mime-type-of]]))

;; body is a string or input stream, headers is a map of lowercased headers.
;; Ring and clj-http both use this format for headers, for example.
(mime-type-of body headers)
```

In this case, Pantomime will try to detect content type from response body first (because there are applications, frameworks
and servers that report content type incorrectly, for example, serve PDFs as `text/html`) and if it fails, will use content
type header.

HTTP headers map must contain "content-type" key for content type header to be used. Most Clojure HTTP client, for instance, [clj-http](https://github.com/dakrone/clj-http),
use lowercase strings for header names so Pantomime follows this convention.


### Extension Recommendation

Pantomime can recommend an extension (one of the well known ones)
for a MIME type:

``` clojure
(require '[pantomime.mime :as pm])

(pm/extension-for-name "application/vnd.ms-excel")
;= ".xls"
(pm/extension-for-name "image/jpeg")
;= ".jpg"
(pm/extension-for-name "application/octet-stream")
;= ".bin"
```


### Parsing and Recognizing Media Types

``` clojure
(ns your.app.namespace
  (:require [pantomime.media :as mt]))

(mt/parse "application/json")

(mt/base-type "text/html; charset=UTF-8") ;; => media type of "text/html"

(mt/application? "application/json")
(mt/application? "application/xhtml+xml")
(mt/application? "application/pdf")
(mt/application? "application/vnd.ms-excel")
(mt/application? (mt/parse "application/json"))

(mt/image? "image/jpeg")
(mt/audio? "audio/mp3")
(mt/video? "video/quicktime")
(mt/text?  "text/plain")
(mt/has-parameters? "text/html; charset=UTF-8") ;; => true
(mt/has-parameters? "text/html") ;; => false
(mt/parameters-of "text/html; charset=UTF-8") ;; => {"charset" "UTF-8"}
(mt/charset-of "text/html; charset=UTF-8") ;; => "UTF-8"
```

### Language Detection

`pantomime.languages` is a new that provides functions for
detecting natural languages:

``` clojure
(require '[pantomime.languages :as pl])

(pl/detect-language "this is English, it should not be hard to detect")
;= "en"

(pl/detect-language "parlez-vous Français")
;= "fr"
```

Note that Tika (and, in turn, Pantomime) supports detection of a limited number
of languages. To get the list of supported languages, use the `pantomime.languages/supported-languages`
var.



## Community

[Pantomime has a mailing list](https://groups.google.com/group/clojure-pantomime). Feel free to join it and ask any questions you may have.

To subscribe for announcements of releases, important changes and so on, please follow [@ClojureWerkz](https://twitter.com/#!/clojurewerkz) on Twitter.



## Pantomime Is a ClojureWerkz Project

Pantomime is part of the [group of libraries known as ClojureWerkz](http://clojurewerkz.org), together with
[Monger](https://github.com/michaelklishin/monger), [Langohr](https://github.com/michaelklishin/langohr), [Neocons](https://github.com/clojurewerkz/neocons), [Elastisch](https://github.com/clojurewerkz/elastisch), [Quartzite](https://github.com/michaelklishin/quartzite) and several others.




## Continuous Integration

[![Continuous Integration status](https://secure.travis-ci.org/michaelklishin/pantomime.png)](http://travis-ci.org/michaelklishin/pantomime)

CI is hosted by [travis-ci.org](http://travis-ci.org)


## Development

Pantomime uses [Leiningen 2](https://github.com/technomancy/leiningen/blob/master/doc/TUTORIAL.md). Make
sure you have it installed and then run tests against all supported Clojure versions using

    lein all test

Then create a branch and make your changes on it. Once you are done with your changes and all
tests pass, submit a pull request on Github.


## License

Copyright (C) 2011-2015 Michael S. Klishin, and the ClojureWerkz team.

Distributed under the Eclipse Public License, the same as Clojure.
