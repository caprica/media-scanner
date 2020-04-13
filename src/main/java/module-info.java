module uk.co.caprica.media.scanner {
    requires com.google.common;

    exports uk.co.caprica.mediascanner;
    exports uk.co.caprica.mediascanner.domain;
    exports uk.co.caprica.mediascanner.meta;
    exports uk.co.caprica.mediascanner.title;

    uses uk.co.caprica.mediascanner.meta.MetaProvider;
    uses uk.co.caprica.mediascanner.title.TitleProvider;

    provides uk.co.caprica.mediascanner.title.TitleProvider with uk.co.caprica.mediascanner.title.DefaultTitleProvider;
}
