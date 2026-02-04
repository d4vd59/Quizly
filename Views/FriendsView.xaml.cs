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

        private void SearchTextBox_GotFocus(object sender, System.Windows.RoutedEventArgs e)
        {
            if (sender is TextBox tb && tb.Text == "Search")
                tb.Text = string.Empty;
        }

        private void SearchTextBox_LostFocus(object sender, System.Windows.RoutedEventArgs e)
        {
            if (sender is TextBox tb && string.IsNullOrWhiteSpace(tb.Text))
                tb.Text = "Search";
        }

        private void Search_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            // Suchlogik hier aufrufen
        }
    }
}
