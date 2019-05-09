package org.wordpress.android.e2e;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.wordpress.android.R;
import org.wordpress.android.e2e.components.MasterbarComponent;
import org.wordpress.android.e2e.pages.EditorPage;
import org.wordpress.android.e2e.pages.MySitesPage;
import org.wordpress.android.support.BaseTest;
import org.wordpress.android.ui.WPLaunchActivity;

import java.time.Instant;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;
import static org.wordpress.android.BuildConfig.E2E_WP_COM_USER_SITE_ADDRESS;
import static org.wordpress.android.support.WPSupportUtils.checkViewHasText;
import static org.wordpress.android.support.WPSupportUtils.sleep;
import static org.wordpress.android.support.WPSupportUtils.waitForElementToNotBeDisplayed;

public class EditorTests extends BaseTest {
    @Rule
    public ActivityTestRule<WPLaunchActivity> mActivityTestRule = new ActivityTestRule<>(WPLaunchActivity.class);

    @Test
    public void testPublishSimplePost() {
        String title = "Hello Espresso!";
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

        loginIfNecessary();

        MasterbarComponent mb = new MasterbarComponent().goToMySitesTab();
        sleep();
        mb.clickBlogPosts();

        new MySitesPage()
                .startNewPost(E2E_WP_COM_USER_SITE_ADDRESS);

        EditorPage editorPage = new EditorPage();
        editorPage.enterTitle(title);
        editorPage.enterContent(content);
        boolean isPublished = editorPage.publishPost();
        assertTrue(isPublished);
    }

    @Test
    public void testPublishFullPost() {
        String title = "Hello Espresso!";
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod "
                         + "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud "
                         + "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
        String category = "Android Test";
        long now = Instant.now().toEpochMilli();
        String tag = "Tag " + now;

        loginIfNecessary();

        MasterbarComponent mb = new MasterbarComponent().goToMySitesTab();
        sleep();
        mb.clickBlogPosts();

        new MySitesPage()
                .startNewPost(E2E_WP_COM_USER_SITE_ADDRESS);

        EditorPage editorPage = new EditorPage();
        editorPage.enterTitle(title);
        editorPage.enterContent(content);
        editorPage.enterImage();
        editorPage.openSettings();

        editorPage.addACategory(category);
        editorPage.addATag(tag);
        editorPage.setFeaturedImage();

        // ----------------------------
        // Verify post settings data
        // ----------------------------
        // Verify Category added
        checkViewHasText(onView(withId(R.id.post_categories)), category);

        // Verify tag added
        checkViewHasText(onView(withId(R.id.post_tags)), tag);

        // Verify the featured image added
        waitForElementToNotBeDisplayed(onView(withText(R.string.post_settings_set_featured_image)));

        // head back to the post
        pressBack();

        // publish
        boolean isPublished = editorPage.publishPost();
        assertTrue(isPublished);
    }
}