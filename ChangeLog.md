## Changes between Pantomime 3.0.0 and 3.1.0 (unreleased)

### Bug Fixes

 * `pantomime.extract/parse` ignored a custom `TikaConfig` when called with a URL input


## Changes between Pantomime 2.11.0 and 3.0.0 (Jan 18, 2025)

### Apache Tika 3.2.x

Apache Tika dependency has been upgraded from 1.19.1 to [3.2.3](https://tika.apache.org/3.2.3/index.html).

This is a major upgrade that includes breaking changes:

 * This Tika version, and therefore Pantomime, requires Java 11+
 * Language detection now uses `OptimaizeLangDetector` instead of deprecated `LanguageIdentifier`
 * Several of Tika's artifacts and transient dependencies have changed
 
### MIME Type Detection Changes

Tika 3.x also has a few MIME detection behavior changes:

 * JavaScript files now detected as `text/javascript` instead of `application/javascript`
 * `robots.txt` files now detected as `text/x-robots` instead of `text/plain`
 
### Clojure 1.12

The library now depends on Clojure 1.12.4 by default.

### Dependency Updates

 * clj-http upgraded to 3.13.1
 * Added managed dependencies for Bouncy Castle libraries


## Changes between Pantomime 2.10.0 and 2.11.0 (March 6th, 2019)

### Apache Tika 1.19.1

Apache Tika dependency has been upgraded to [1.19.1](https://tika.apache.org/1.19.1/index.html).

GitHub issue: [#29](https://github.com/michaelklishin/pantomime/issues/29)

### Clojure 1.10

The library now depends on Clojure 1.10.

## Changes between Pantomime 2.9.0 and 2.10.0 (Jan 19th, 2018)

### Apache Tika 1.17

Apache Tika dependency has been upgraded to [1.14](https://tika.apache.org/1.17/index.html).

## Clojure 1.9

The library now depends on Clojure 1.9.


## Changes between Pantomime 2.8.0 and 2.9.0 (Feb 2nd, 2017)

### Apache Tika 1.14

Apache Tika dependency has been upgraded to [1.14](https://tika.apache.org/1.14/index.html).

### `TikaConfig` Instance Support

Several functions now accept `TikaConfig` instances.

Contributed by ejshoen.

### Clojure 1.8

The library now depends on Clojure 1.8.

### Namespaced Keys

Extraction functions now return namespaced keywords.

Contributed by Ole Krüger.

### Switch to `instance-of?` instead of `type-instance?`

Contributed by Dorian Taylor.


## Changes between Pantomime 2.7.0 and 2.8.0

### Apache Tika 1.11

Apache Tika dependency has been upgraded to [version 1.11](http://www.apache.org/dist/tika/CHANGES-1.11.txt).

### MIME Pattern Extension

`pantomime.mime/add-pattern` is a new function that makes
it possible to extent MIME patterns used by the library:

``` clojure
(require '[pantomime.mime :as pm])

(pm/add-pattern "text/lorem-ipsum" ".+\\.ipsum$" "lorem.ipsum")
```

Contributed by Daniel Woelfel and Tommi Reinikainen.

## Clojure 1.7

The library now depends on Clojure 1.7.


## Changes between Pantomime 2.6.0 and 2.7.0

### Apache Tika 1.9

Apache Tika dependency has been upgraded to [version 1.9](http://www.apache.org/dist/tika/CHANGES-1.9.txt).


## Changes between Pantomime 2.5.0 and 2.6.0

### Apache Tika 1.8

Apache Tika dependency has been upgraded to [version 1.8](http://www.apache.org/dist/tika/CHANGES-1.8.txt).


## Changes between Pantomime 2.4.0 and 2.5.0

### Content Extraction API

Pantomime now provdes access to Tika's content extraction
functionality via `pantomime.extract/parse`:

``` clojure
(require [clojure.java.io :as io]
         [pantomime.extract :as extract])

(pprint (extract/parse "test/resources/pdf/qrl.pdf"))

;= {:producer ("GNU Ghostscript 7.05"),
;=  :pdf:pdfversion ("1.2"),
;=  :dc:title ("main.dvi"),
;=  :dc:format ("application/pdf; version=1.2"),
;=  :xmp:creatortool ("dvips(k) 5.86 Copyright 1999 Radical Eye Software"),
;=  :pdf:encrypted ("false"),
;=  ...
;=  :text "\nQuickly Reacquirable Locks∗\n\nDave Dice Mark Moir ... "
;= }
```

If extraction fails, `extract.parse` will return the following:

``` clojure
{:text "",
 :content-type ("application/octet-stream"),
 :x-parsed-by ("org.apache.tika.parser.EmptyParser")}
```

`extract/parse` is a simple interface to Tika's own
[Parser.parse method](https://tika.apache.org/1.7/parser.html).

Contributed by Joshua Thayer.




## Changes between Pantomime 2.3.0 and 2.4.0

### Apache Tika 1.7

Apache Tika dependency has been upgraded to [version 1.7](http://www.apache.org/dist/tika/CHANGES-1.7.txt).



## Changes between Pantomime 2.2.0 and 2.3.0

### Extension Detection From MIME Type

`pantomime.mime/extension-for-name` is a new function that suggests
common extensions for MIME type names:

``` clojure
(require '[pantomime.mime :as pm])

(pm/extension-for-name "application/vnd.ms-excel")
;= ".xls"
(pm/extension-for-name "image/jpeg")
;= ".jpg"
(pm/extension-for-name "application/octet-stream")
;= ".bin"
```


## Changes between Pantomime 2.1.0 and 2.2.0

## Clojure 1.6

The library now depends on Clojure 1.6.



## Changes between Pantomime 2.0.0 and 2.1.0

### Apache Tika 1.5

Apache Tika dependency has been upgraded to [version 1.5](http://www.apache.org/dist/tika/CHANGES-1.5.txt).



## Changes between Pantomime 1.8.0 and 2.0.0

### Clojure 1.3 is No Longer Supported

Pantomime `2.0` drops support for Clojure 1.3.

### Language Detection Support

`pantomime.languages` is a new namespace that provides functions for
detecting natural languages:

``` clojure
(require '[pantomime.languages :as pl])

(pl/detect-language "this is English, it should not be hard to detect")
;= "en"

(pl/detect-language "parlez-vous Français")
;= "fr"
```



## Changes between Pantomime 1.7.0 and 1.8.0

### Apache Tika 1.4

Apache Tika dependency has been upgraded to [version 1.4](http://www.apache.org/dist/tika/CHANGES-1.4.txt).


## Changes between Pantomime 1.6.0 and 1.7.0

### Clojure 1.5.1 By Default

Pantomime now depends on `org.clojure/clojure` version `1.5.1` which
includes an important bug fix.



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
