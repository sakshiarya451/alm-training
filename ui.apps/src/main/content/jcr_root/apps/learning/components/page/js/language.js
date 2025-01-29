use(function () {
    function getPageLocale() {
        var allLocales = [
            "ar-SA", "bg-BG", "zh-CN", "zh-TW", "hr-HR", "cs-CZ", "da-DK", "nl-BE",
            "en-US", "fi-FI", "fr-CA", "fr-FR", "de-DE", "el-GR", "he-IL", "hi-IN",
            "hu-HU", "id-ID", "it-IT", "ja-JP", "ko-KR", "nb-NO", "pl-PL", "pt-BR",
            "ro-RO", "ru-RU", "sk-SK", "es-ES", "sv-SE", "th-TH", "tr-TR", "uk-UA",
            "vi-VN", "es-LA", "es-XL", "nl-NL","pt-PT",  "no-NO","sl-SL", "am-ET",
            "en-AU","sr-SP","zh-HK","en-GB"
        ];
        var request = sling.getRequest();
        var english = "en-US";
        if (!request)
            return english;

        var path = request.getRequestURI().toString();
        var segments = path.split("/");
        var languageCode = segments[segments.length - 1];

        for (var i = 0; i <= allLocales.length; i++) {
            if (allLocales[i] == languageCode)
                return allLocales[i];
        }
        if (languageCode.startsWith("ar-")) {
            return languageCode;
          }
        return english;
    }

    return {
        languageCode: getPageLocale()
    };
});
