using Microsoft.Win32;
using System;
using System.Text.RegularExpressions;
using System.Windows.Controls;
using System.Windows.Media.Imaging;

namespace Quizly.Views
{
    public partial class RegisterView : UserControl
    {
        private readonly MainWindow _main;
        private string _avatarPath; // nur GUI (merken)

        public RegisterView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
            AvatarPreview.Source = null;
        }

        private void Back_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.GoLogin();
        }

        private void GoLogin_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.GoLogin();
        }

        private void PickAvatar_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            var dlg = new OpenFileDialog
            {
                Filter = "Bilder (*.png;*.jpg;*.jpeg)|*.png;*.jpg;*.jpeg",
                Title = "Avatar auswählen"
            };

            if (dlg.ShowDialog() == true)
            {
                _avatarPath = dlg.FileName;

                try
                {
                    var bmp = new BitmapImage();
                    bmp.BeginInit();
                    bmp.CacheOption = BitmapCacheOption.OnLoad;
                    bmp.UriSource = new Uri(_avatarPath);
                    bmp.EndInit();
                    bmp.Freeze();

                    AvatarPreview.Source = bmp;
                }
                catch { }
            }
        }

        private void Create_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            ErrorText.Text = "";

            var name = NameBox.Text.Trim();
            var username = UsernameBox.Text.Trim();
            var email = EmailBox.Text.Trim();
            var pw = PasswordBox.Password;
            var pw2 = PasswordRepeatBox.Password;

            // GUI-only validation (nur Anzeige)
            if (string.IsNullOrWhiteSpace(name) ||
                string.IsNullOrWhiteSpace(username) ||
                string.IsNullOrWhiteSpace(email) ||
                string.IsNullOrWhiteSpace(pw) ||
                string.IsNullOrWhiteSpace(pw2))
            {
                ErrorText.Text = "Bitte alle Felder ausfüllen.";
                return;
            }

            if (!Regex.IsMatch(email, @"^[^@\s]+@[^@\s]+\.[^@\s]+$"))
            {
                ErrorText.Text = "Bitte eine gültige E-Mail eingeben.";
                return;
            }

            if (pw != pw2)
            {
                ErrorText.Text = "Passwörter stimmen nicht überein.";
                return;
            }

            // ✅ Frontend-only: wir tun so als ob Registrierung ok ist
            // Später: hier Backend aufrufen (Python API)
            _main.GoHome();
        }
    }
}
