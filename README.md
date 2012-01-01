# What is Pantomime

Pantomime is a tiny Clojure library that deals with MIME types.


## Maven Artifact

Leiningen:

```clojure
[com.novemberain/pantomime "1.0.0"]
```


## Supported Clojure versions

Pantomime was built for Clojure 1.3 and later, although it is a really small library that should
work fine on 1.2.x as well.


## Usage

### Detecting MIME type

``` clojure
(ns your.app.namespace
  (:use [pantomime.core]))


(mime-type-of "filename.pdf")
(mime-type-of (File. "some/file/path.pdf"))
(mime-type-of (File. "some/file/without/extension"))
(mime-type-of (URL. "http://domain.com/some/url/path.pdf"))
```


## License

Copyright (C) 2011 Michael S. Klishin

Distributed under the Eclipse Public License, the same as Clojure.
