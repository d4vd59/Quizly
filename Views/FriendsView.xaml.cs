using System.Windows.Controls;

namespace Quizly.Views
{
    public partial class FriendsView : UserControl
    {
        public FriendsView(MainWindow main)
        {
            InitializeComponent();
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
            InviteOverlay.Visibility = System.Windows.Visibility.Collapsed;
        }
    }
}
