using System;
using System.Windows.Controls;
using Newtonsoft.Json.Linq;

namespace Quizly.Views
{
    public partial class LoginView : UserControl
    {
        private readonly MainWindow _main;

        public LoginView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private async void Login_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            ErrorText.Text = "";

            var user = EmailBox.Text.Trim();  // Oder UsernameBox, je nach Feld
            var pw = PasswordBox.Password;

            if (string.IsNullOrWhiteSpace(user) || string.IsNullOrWhiteSpace(pw))
            {
                ErrorText.Text = "Bitte E-Mail und Passwort eingeben.";
                return;
            }

            // ✅ NEU: Backend-Aufruf für echten Login
            try
            {
                var backend = new PythonBackend();
                string resultJson = await backend.SigninAsync(user, pw);
                
                // ✅ DEBUG: JSON-Antwort ausgeben
                System.Diagnostics.Debug.WriteLine($"API Response: {resultJson}");
                
                var result = JObject.Parse(resultJson);
        
                // ✅ NEU: Prüfe zuerst auf Erfolg (X-Auth-Token vorhanden)
                if (result["X-Auth-Token"] != null)
                {
                    // Erfolg: Navigiere zu Home
                    _main.GoHome();
                    return;
                }
        
                // ✅ NEU: Bei Fehlern zeige Message oder error
                string errorMsg = result["Message"]?.ToString() ?? result["error"]?.ToString() ?? "Unbekannter Fehler";
                ErrorText.Text = errorMsg;
            }
            catch (Exception ex)
            {
                ErrorText.Text = $"Login-Fehler: {ex.Message}";
            }
        }

        private void Register_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.ShowBottomBar(false);
            _main.NavigateTo(new RegisterView(_main));
        }

        private void Guest_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.GoHome();
        }
    }
}
