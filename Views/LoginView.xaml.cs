using System.Windows.Controls;

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

        private void Login_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            ErrorText.Text = "";

            var email = EmailBox.Text.Trim();
            var pw = PasswordBox.Password;

            // GUI-only: minimal check
            if (string.IsNullOrWhiteSpace(email) || string.IsNullOrWhiteSpace(pw))
            {
                ErrorText.Text = "Bitte E-Mail und Passwort eingeben.";
                return;
            }

            // ✅ Frontend-only: Wir tun so als ob Login ok ist
            // Später: hier Backend aufrufen und echte Prüfung machen
            _main.GoHome();
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
