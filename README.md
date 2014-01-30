media-scanner
=============

Scan directories recursively for media, with pluggable title and meta-data
parsing.

I've written a lot of media player projects, and I've written code to scan for
media many, many, many times. So instead of writing it many more times, here is
this project.

Usage is very simple:

```
MediaSet mediaSet = MediaScanner.create()
    .directory("/home/movies")
    .directory("/home/tv")
    .matching("glob:**/*.{iso,mp4,avi,wmv,flv}")
    .findMedia()
    .collectMeta()
    .mediaSet();
```

Since the names of media files are not always what you want to display for the
media titles, the parsing of those titles is accomplished by a pluggable
service loader implementation.

By default these titles will simply be the filename of the media file without
the file extension - but if you add your own implementation of a title provider
to your application classpath, it will be loaded by the standard JDK
`ServiceLoader` mechanism and used instead.

You could provide an implementation that uses string splitting,
regular-expression parsing, grammar parsing, or whatever else you can think of.

Similarly, the optional collection of meta data for each media item is also
pluggable.

By default no meta data is provided, but if you add your own implementation of
a meta data provider to your application classpath, it will be loaded by the
standard JDK `ServiceLoader` mechanism and used instead. In fact, you can have
any number of such implementations and they will all be tried in turn. In this
way you can have a meta data provider for movie details and a separate one for
artwork, for example.
