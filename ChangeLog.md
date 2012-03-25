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
