using System.Windows;
using System.Windows.Controls;

namespace Quizly.Views
{
    public partial class FriendsView : UserControl
    {
        private readonly MainWindow _main;

        public FriendsView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private void SearchTextBox_GotFocus(object sender, RoutedEventArgs e)
        {
            // Placeholder wird automatisch durch DataTrigger ausgeblendet
        }

        private void SearchTextBox_LostFocus(object sender, RoutedEventArgs e)
        {
            // Placeholder wird automatisch durch DataTrigger eingeblendet
        }

        private void Search_Click(object sender, RoutedEventArgs e)
        {
            string username = SearchTextBox.Text;
            if (!string.IsNullOrWhiteSpace(username))
            {
                // TODO: Benutzer suchen
                MessageBox.Show($"Suche nach: {username}", "Freunde suchen");
            }
        }

        private void Invite_Click(object sender, RoutedEventArgs e)
        {
            InviteOverlay.Visibility = Visibility.Visible;
        }

        private void InviteSend_Click(object sender, RoutedEventArgs e)
        {
            // TODO: Einladung versenden
            InviteOverlay.Visibility = Visibility.Collapsed;
        }

        private void CloseOverlay_Click(object sender, RoutedEventArgs e)
        {
            InviteOverlay.Visibility = Visibility.Collapsed;
        }
    }
}
