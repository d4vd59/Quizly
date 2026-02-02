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

        private void Invite_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            InviteOverlay.Visibility = System.Windows.Visibility.Visible;
        }

        private void CloseOverlay_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            InviteOverlay.Visibility = System.Windows.Visibility.Collapsed;
        }

        private void InviteSend_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            // Demo: einfach schließen
            InviteOverlay.Visibility = System.Windows.Visibility.Collapsed;

            // Optional: direkt ein "Freunde Match" starten:
            _main.NavigateTo(new DuelOverviewView(_main));
        }
    }
}
