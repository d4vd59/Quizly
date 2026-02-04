using System;
using System.Windows;

namespace Quizly
{
    /// <summary>
    /// Interaktionslogik für "App.xaml"
    /// </summary>
    public partial class App : Application
    {
        // ✅ Globale Backend-Instanz
        public static PythonBackend Backend { get; private set; }
        
        // ✅ Session-Daten
        public static string CurrentUser { get; set; }
        public static string AuthToken { get; set; }

        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);
            
            // ✅ Backend beim App-Start initialisieren
            Backend = new PythonBackend();
        }
    }
}
