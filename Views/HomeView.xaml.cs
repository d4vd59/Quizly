using Microsoft.Win32;
using System;
using System.Windows.Controls;
using System.Windows.Media.Imaging;

namespace Quizly.Views
{
    public partial class HomeView : UserControl
    {
        private readonly MainWindow _main;

        public HomeView(MainWindow main)
        {
            InitializeComponent();
            _main = main;

            // Default: kein Bild -> Kreis bleibt sichtbar
            AvatarImage.Source = null;
        }

        private void Avatar_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            var dlg = new OpenFileDialog
            {
                Filter = "Bilder (*.png;*.jpg;*.jpeg)|*.png;*.jpg;*.jpeg",
                Title = "Avatar auswählen"
            };

            if (dlg.ShowDialog() == true)
            {
                try
                {
                    var bmp = new BitmapImage();
                    bmp.BeginInit();
                    bmp.CacheOption = BitmapCacheOption.OnLoad;
                    bmp.UriSource = new Uri(dlg.FileName);
                    bmp.EndInit();
                    bmp.Freeze();

                    AvatarImage.Source = bmp;
                }
                catch { }
            }
        }

        private void NewMatch_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            NewGameOverlay.Visibility = System.Windows.Visibility.Visible;
        }

        private void CloseOverlay_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            NewGameOverlay.Visibility = System.Windows.Visibility.Collapsed;
        }

        private void FindOpponent_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            NewGameOverlay.Visibility = System.Windows.Visibility.Collapsed;
            _main.NavigateTo(new MatchmakingView(_main));
        }

        private void PlayWithFriend_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            NewGameOverlay.Visibility = System.Windows.Visibility.Collapsed;
            _main.GoFriends();
        }

        private void InviteFriends_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            NewGameOverlay.Visibility = System.Windows.Visibility.Collapsed;
            _main.GoFriends();
        }

        private void OpenMatch_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new DuelOverviewView(_main));
        }
    }
}
