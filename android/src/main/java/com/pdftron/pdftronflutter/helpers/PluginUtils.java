package com.pdftron.pdftronflutter.helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pdftron.common.PDFNetException;
import com.pdftron.fdf.FDFDoc;
import com.pdftron.pdf.Annot;
import com.pdftron.pdf.Field;
import com.pdftron.pdf.PDFDoc;
import com.pdftron.pdf.PDFViewCtrl;
import com.pdftron.pdf.Page;
import com.pdftron.pdf.Rect;
import com.pdftron.pdf.ViewChangeCollection;
import com.pdftron.pdf.annots.Markup;
import com.pdftron.pdf.config.PDFViewCtrlConfig;
import com.pdftron.pdf.config.ToolConfig;
import com.pdftron.pdf.config.ToolManagerBuilder;
import com.pdftron.pdf.config.ViewerConfig;
import com.pdftron.pdf.controls.PdfViewCtrlTabFragment2;
import com.pdftron.pdf.controls.PdfViewCtrlTabHostFragment2;
import com.pdftron.pdf.controls.ReflowControl;
import com.pdftron.pdf.controls.ThumbnailsViewFragment;
import com.pdftron.pdf.controls.UserCropSelectionDialogFragment;
import com.pdftron.pdf.dialog.RotateDialogFragment;
import com.pdftron.pdf.dialog.ViewModePickerDialogFragment;
import com.pdftron.pdf.dialog.pdflayer.PdfLayerDialog;
import com.pdftron.pdf.dialog.signature.SignatureDialogFragment;
import com.pdftron.pdf.model.AnnotStyle;
import com.pdftron.pdf.tools.AdvancedShapeCreate;
import com.pdftron.pdf.tools.AnnotEditRectGroup;
import com.pdftron.pdf.tools.Eraser;
import com.pdftron.pdf.tools.FreehandCreate;
import com.pdftron.pdf.tools.QuickMenuItem;
import com.pdftron.pdf.tools.Tool;
import com.pdftron.pdf.tools.ToolManager;
import com.pdftron.pdf.tools.AnnotManager;
import com.pdftron.pdf.utils.AnalyticsHandlerAdapter;
import com.pdftron.pdf.utils.AnnotUtils;
import com.pdftron.pdf.utils.BookmarkManager;
import com.pdftron.pdf.utils.DialogGoToPage;
import com.pdftron.pdf.utils.CommonToast;
import com.pdftron.pdf.utils.PdfViewCtrlSettingsManager;
import com.pdftron.pdf.utils.StampManager;
import com.pdftron.pdf.utils.Utils;
import com.pdftron.pdf.utils.ViewerUtils;
import com.pdftron.pdf.widget.bottombar.builder.BottomBarBuilder;
import com.pdftron.pdf.widget.toolbar.builder.AnnotationToolbarBuilder;
import com.pdftron.pdf.widget.toolbar.builder.ToolbarButtonType;
import com.pdftron.pdf.widget.toolbar.component.DefaultToolbars;
import com.pdftron.pdftronflutter.R;
import com.pdftron.pdf.PDFDraw;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class PluginUtils {

    public static final String KEY_LICENSE_KEY = "licenseKey";
    public static final String KEY_DOCUMENT = "document";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CONFIG = "config";
    public static final String KEY_XFDF_COMMAND = "xfdfCommand";
    public static final String KEY_XFDF = "xfdf";
    public static final String KEY_BOOKMARK_JSON = "bookmarkJson";
    public static final String KEY_PAGE_NUMBER = "pageNumber";
    public static final String KEY_TOOL_MODE = "toolMode";
    public static final String KEY_FIELD_NAMES = "fieldNames";
    public static final String KEY_FLAG = "flag";
    public static final String KEY_FLAG_VALUE = "flagValue";
    public static final String KEY_FIELDS = "fields";
    public static final String KEY_ANNOTATION_LIST = "annotations";
    public static final String KEY_ANNOTATION = "annotation";
    public static final String KEY_FORMS_ONLY = "formsOnly";
    public static final String KEY_ANNOTATIONS_WITH_FLAGS = "annotationsWithFlags";
    public static final String KEY_ANNOTATION_PROPERTIES = "annotationProperties";
    public static final String KEY_LEADING_NAV_BUTTON_ICON = "leadingNavButtonIcon";
    public static final String KEY_DPI = "dpi";
    public static final String KEY_EXPORT_FORMAT = "exportFormat";
    public static final String KEY_EXPORT_FORMAT_BMP = "BMP";
    public static final String KEY_EXPORT_FORMAT_JPEG = "JPEG";
    public static final String KEY_EXPORT_FORMAT_PNG = "PNG";
    public static final String KEY_ZOOM_LIMIT_MODE = "zoomLimitMode";
    public static final String KEY_MAXIMUM = "maximum";
    public static final String KEY_MINIMUM = "minimum";
    public static final String KEY_ZOOM_LIMIT_MODE_NONE = "none";
    public static final String KEY_ZOOM_LIMIT_MODE_ABSOLUTE = "absolute";
    public static final String KEY_ZOOM_LIMIT_MODE_RELATIVE = "relative";
    public static final String KEY_RED = "red";
    public static final String KEY_GREEN = "green";
    public static final String KEY_BLUE = "blue";
    public static final String KEY_ANIMATED = "animated";

    public static final String KEY_REQUESTED_ORIENTATION = "requestedOrientation";

    public static final String KEY_CONFIG_DISABLED_ELEMENTS = "disabledElements";
    public static final String KEY_CONFIG_DISABLED_TOOLS = "disabledTools";
    public static final String KEY_CONFIG_MULTI_TAB_ENABLED = "multiTabEnabled";
    public static final String KEY_CONFIG_CUSTOM_HEADERS = "customHeaders";
    public static final String KEY_CONFIG_FIT_MODE = "fitMode";
    public static final String KEY_CONFIG_LAYOUT_MODE = "layoutMode";
    public static final String KEY_CONFIG_TABLET_LAYOUT_ENABLED = "tabletLayoutEnabled";
    public static final String KEY_CONFIG_INITIAL_PAGE_NUMBER = "initialPageNumber";
    public static final String KEY_CONFIG_IS_BASE_64_STRING = "isBase64String";
    public static final String KEY_CONFIG_IS_DARK_MODE_STRING = "isDarkModeString"; //Hatn
    public static final String KEY_CONFIG_BASE_64_FILE_EXTENSION = "base64FileExtension";
    public static final String KEY_CONFIG_HIDE_THUMBNAIL_FILTER_MODES = "hideThumbnailFilterModes";
    public static final String KEY_CONFIG_LONG_PRESS_MENU_ENABLED = "longPressMenuEnabled";
    public static final String KEY_CONFIG_LONG_PRESS_MENU_ITEMS = "longPressMenuItems";
    public static final String KEY_CONFIG_OVERRIDE_LONG_PRESS_MENU_BEHAVIOR = "overrideLongPressMenuBehavior";
    public static final String KEY_CONFIG_HIDE_ANNOTATION_MENU = "hideAnnotationMenu";
    public static final String KEY_CONFIG_ANNOTATION_MENU_ITEMS = "annotationMenuItems";
    public static final String KEY_CONFIG_OVERRIDE_ANNOTATION_MENU_BEHAVIOR = "overrideAnnotationMenuBehavior";
    public static final String KEY_CONFIG_EXCLUDED_ANNOTATION_LIST_TYPES = "excludedAnnotationListTypes";
    public static final String KEY_CONFIG_EXPORT_PATH = "exportPath";
    public static final String KEY_CONFIG_OPEN_URL_PATH = "openUrlPath";
    public static final String KEY_CONFIG_OPEN_SAVED_COPY_IN_NEW_TAB = "openSavedCopyInNewTab";
    public static final String KEY_CONFIG_MAX_TAB_COUNT = "maxTabCount";
    public static final String KEY_CONFIG_AUTO_SAVE_ENABLED = "autoSaveEnabled";
    public static final String KEY_CONFIG_SHOW_DOCUMENT_SAVED_TOAST = "showDocumentSavedToast";
    public static final String KEY_CONFIG_PAGE_CHANGE_ON_TAP = "pageChangeOnTap";
    public static final String KEY_CONFIG_SHOW_SAVED_SIGNATURES = "showSavedSignatures";
    public static final String KEY_CONFIG_SIGNATURE_PHOTO_PICKER_ENABLED = "signaturePhotoPickerEnabled";
    public static final String KEY_CONFIG_SIGNATURE_TYPING_ENABLED = "signatureTypingEnabled";
    public static final String KEY_CONFIG_USE_STYLUS_AS_PEN = "useStylusAsPen";
    public static final String KEY_CONFIG_SIGN_SIGNATURE_FIELD_WITH_STAMPS = "signSignatureFieldWithStamps";
    public static final String KEY_CONFIG_SIGNATURE_COLORS = "signatureColors";
    public static final String KEY_CONFIG_SELECT_ANNOTATION_AFTER_CREATION = "selectAnnotationAfterCreation";
    public static final String KEY_CONFIG_PAGE_INDICATOR_ENABLED = "pageIndicatorEnabled";
    public static final String KEY_CONFIG_SHOW_QUICK_NAVIGATION_BUTTON = "showQuickNavigationButton";
    public static final String KEY_CONFIG_FOLLOW_SYSTEM_DARK_MODE = "followSystemDarkMode";
    public static final String KEY_CONFIG_DOWNLOAD_DIALOG_ENABLED = "downloadDialogEnabled";
    public static final String KEY_CONFIG_SINGLE_LINE_TOOLBAR = "singleLineToolbar";
    public static final String KEY_CONFIG_ANNOTATION_TOOLBARS = "annotationToolbars";
    public static final String KEY_CONFIG_HIDE_DEFAULT_ANNOTATION_TOOLBARS = "hideDefaultAnnotationToolbars";
    public static final String KEY_CONFIG_HIDE_ANNOTATION_TOOLBAR_SWITCHER = "hideAnnotationToolbarSwitcher";
    public static final String KEY_CONFIG_INITIAL_TOOLBAR = "initialToolbar";
    public static final String KEY_CONFIG_HIDE_TOP_TOOLBARS = "hideTopToolbars";
    public static final String KEY_CONFIG_HIDE_TOOLBARS_ON_TAP = "hideToolbarsOnTap";
    public static final String KEY_CONFIG_HIDE_TOP_APP_NAV_BAR = "hideTopAppNavBar";
    public static final String KEY_CONFIG_HIDE_BOTTOM_TOOLBAR = "hideBottomToolbar";
    public static final String KEY_CONFIG_HIDE_PRESET_BAR = "hidePresetBar";
    public static final String KEY_CONFIG_BOTTOM_TOOLBAR = "bottomToolbar";
    public static final String KEY_CONFIG_SHOW_LEADING_NAV_BUTTON = "showLeadingNavButton";
    public static final String KEY_CONFIG_REMEMBER_LAST_USED_TOOL = "rememberLastUsedTool";
    public static final String KEY_CONFIG_DOCUMENT_SLIDER_ENABLED = "documentSliderEnabled";
    public static final String KEY_CONFIG_READ_ONLY = "readOnly";
    public static final String KEY_CONFIG_THUMBNAIL_VIEW_EDITING_ENABLED = "thumbnailViewEditingEnabled";
    public static final String KEY_CONFIG_ANNOTATION_AUTHOR = "annotationAuthor";
    public static final String KEY_CONFIG_CONTINUOUS_ANNOTATION_EDITING = "continuousAnnotationEditing";
    public static final String KEY_CONFIG_ANNOTATION_PERMISSION_CHECK_ENABLED = "annotationPermissionCheckEnabled";
    public static final String KEY_CONFIG_ANNOTATIONS_LIST_EDITING_ENABLED = "annotationsListEditingEnabled";
    public static final String KEY_CONFIG_USER_BOOKMARKS_LIST_EDITING_ENABLED = "userBookmarksListEditingEnabled";
    public static final String KEY_CONFIG_OUTLINE_LIST_EDITING_ENABLED = "outlineListEditingEnabled";
    public static final String KEY_CONFIG_SHOW_NAVIGATION_LIST_AS_SIDE_PANEL_ON_LARGE_DEVICES = "showNavigationListAsSidePanelOnLargeDevices";
    public static final String KEY_CONFIG_OVERRIDE_BEHAVIOR = "overrideBehavior";
    public static final String KEY_CONFIG_TAB_TITLE = "tabTitle";
    public static final String KEY_CONFIG_PERMANENT_PAGE_NUMBER_INDICATOR = "pageNumberIndicatorAlwaysVisible";
    public static final String KEY_CONFIG_DISABLE_EDITING_BY_ANNOTATION_TYPE = "disableEditingByAnnotationType";
    public static final String KEY_CONFIG_ANNOTATIONS_LIST_FILTER_ENABLED = "annotationsListFilterEnabled";
    public static final String KEY_CONFIG_HIDE_VIEW_MODE_ITEMS = "hideViewModeItems";
    public static final String KEY_CONFIG_DEFAULT_ERASER_TYPE = "defaultEraserType";
    public static final String KEY_CONFIG_AUTO_RESIZE_FREE_TEXT_ENABLED = "autoResizeFreeTextEnabled";
    public static final String KEY_CONFIG_RESTRICT_DOWNLOAD_USAGE = "restrictDownloadUsage";
    public static final String KEY_CONFIG_REFLOW_ORIENTATION = "reflowOrientation";
    public static final String KEY_CONFIG_IMAGE_IN_REFLOW_MODE_ENABLED = "imageInReflowModeEnabled";
    public static final String KEY_CONFIG_ANNOTATION_MANAGER_ENABLED = "annotationManagerEnabled";
    public static final String KEY_CONFIG_USER_ID = "userId";
    public static final String KEY_CONFIG_USER_NAME = "userName";
    public static final String KEY_CONFIG_ANNOTATION_MANAGER_UNDO_MODE = "annotationManagerUndoMode";
    public static final String KEY_CONFIG_ANNOTATION_MANAGER_EDIT_MODE = "annotationManagerEditMode";
    public static final String KEY_CONFIG_ANNOTATION_TOOLBAR_GRAVITY = "annotationToolbarAlignment";
    public static final String KEY_CONFIG_QUICK_BOOKMARK_CREATION = "quickBookmarkCreation";
    public static final String KEY_CONFIG_FULL_SCREEN_MODE_ENABLED = "fullScreenModeEnabled";
    public static final String KEY_CONFIG_TOP_APP_NAV_BAR_RIGHT_BAR = "topAppNavBarRightBar";

    // Hygen Generated Config Constants
    public static final String KEY_CONFIG_MAX_SIGNATURE_COUNT = "maxSignatureCount";

    public static final String KEY_X1 = "x1";
    public static final String KEY_Y1 = "y1";
    public static final String KEY_X2 = "x2";
    public static final String KEY_Y2 = "y2";
    public static final String KEY_X = "x";
    public static final String KEY_Y = "y";
    public static final String KEY_ZOOM = "zoom";
    public static final String KEY_WIDTH = "width";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_RECT = "rect";
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENTS = "contents";
    public static final String KEY_CONTENT_RECT = "contentRect";
    public static final String KEY_ROTATION = "rotation";

    public static final String KEY_FIELD_NAME = "fieldName";
    public static final String KEY_FIELD_VALUE = "fieldValue";

    public static final String KEY_PREVIOUS_PAGE_NUMBER = "previousPageNumber";

    public static final String KEY_ANNOTATION_ID = "id";

    public static final String KEY_ACTION_ADD = "add";
    public static final String KEY_ACTION_MODIFY = "modify";
    public static final String KEY_ACTION_DELETE = "delete";
    public static final String KEY_ACTION = "action";

    public static final String KEY_DATA = "data";

    public static final String KEY_ANNOTATION_FLAG_LISTS = "flags";
    public static final String KEY_ANNOTATION_FLAG = "flag";
    public static final String KEY_ANNOTATION_FLAG_VALUE = "flagValue";

    public static final String BEHAVIOR_LINK_PRESS = "linkPress";
    public static final String KEY_LINK_BEHAVIOR_DATA = "url";

    public static final String KEY_ANNOTATION_MENU_ITEM = "annotationMenuItem";

    public static final String KEY_LONG_PRESS_MENU_ITEM = "longPressMenuItem";
    public static final String KEY_LONG_PRESS_TEXT = "longPressText";

    public static final String KEY_PATH = "path";

    public static final String KEY_GRAVITY_START = "GravityStart";
    public static final String KEY_GRAVITY_END = "GravityEnd";

    public static final String KEY_HORIZONTAL_SCROLL_POSITION = "horizontalScrollPosition";
    public static final String KEY_VERTICAL_SCROLL_POSITION = "verticalScrollPosition";

    public static final String KEY_SEARCH_STRING = "searchString";
    public static final String KEY_MATCH_CASE = "matchCase";
    public static final String KEY_MATCH_WHOLE_WORD = "matchWholeWord";

    // Hygen Generated Method Parameters
    public static final String KEY_LAYOUT_MODE = "layoutMode";

    public static final String KEY_FIT_MODE = "fitMode";

    // Hatn
    public static final String KEY_IS_DARK_MODE = "isDarkMode";


    public static final String EVENT_EXPORT_ANNOTATION_COMMAND = "export_annotation_command_event";
    public static final String EVENT_EXPORT_BOOKMARK = "export_bookmark_event";
    public static final String EVENT_DOCUMENT_LOADED = "document_loaded_event";
    public static final String EVENT_DOCUMENT_ERROR = "document_error_event";
    public static final String EVENT_ANNOTATION_CHANGED = "annotation_changed_event";
    public static final String EVENT_ANNOTATIONS_SELECTED = "annotations_selected_event";
    public static final String EVENT_FORM_FIELD_VALUE_CHANGED = "form_field_value_changed_event";
    public static final String EVENT_BEHAVIOR_ACTIVATED = "behavior_activated_event";
    public static final String EVENT_LONG_PRESS_MENU_PRESSED = "long_press_menu_pressed_event";
    public static final String EVENT_ANNOTATION_MENU_PRESSED = "annotation_menu_pressed_event";
    public static final String EVENT_LEADING_NAV_BUTTON_PRESSED = "leading_nav_button_pressed_event";
    public static final String EVENT_PAGE_CHANGED = "page_changed_event";
    public static final String EVENT_ZOOM_CHANGED = "zoom_changed_event";
    public static final String EVENT_PAGE_MOVED = "page_moved_event";
    public static final String EVENT_ANNOTATION_TOOLBAR_ITEM_PRESSED = "annotation_toolbar_item_pressed_event";
    public static final String EVENT_SCROLL_CHANGED = "scroll_changed_event";
    // Hygen Generated Event Listeners
    public static final String EVENT_APP_BAR_BUTTON_PRESSED = "app_bar_button_pressed_event";

    public static final String FUNCTION_GET_PLATFORM_VERSION = "getPlatformVersion";
    public static final String FUNCTION_GET_VERSION = "getVersion";
    public static final String FUNCTION_INITIALIZE = "initialize";
    public static final String FUNCTION_OPEN_DOCUMENT = "openDocument";
    public static final String FUNCTION_IMPORT_ANNOTATION_COMMAND = "importAnnotationCommand";
    public static final String FUNCTION_IMPORT_BOOKMARK_JSON = "importBookmarkJson";
    public static final String FUNCTION_SAVE_DOCUMENT = "saveDocument";
    public static final String FUNCTION_COMMIT_TOOL = "commitTool";
    public static final String FUNCTION_GET_PAGE_COUNT = "getPageCount";
    public static final String FUNCTION_HANDLE_BACK_BUTTON = "handleBackButton";
    public static final String FUNCTION_UNDO = "undo";
    public static final String FUNCTION_REDO = "redo";
    public static final String FUNCTION_CAN_UNDO = "canUndo";
    public static final String FUNCTION_CAN_REDO = "canRedo";
    public static final String FUNCTION_GET_PAGE_CROP_BOX = "getPageCropBox";
    public static final String FUNCTION_SET_CURRENT_PAGE = "setCurrentPage";
    public static final String FUNCTION_GET_DOCUMENT_PATH = "getDocumentPath";
    public static final String FUNCTION_SET_TOOL_MODE = "setToolMode";
    public static final String FUNCTION_SET_FLAG_FOR_FIELDS = "setFlagForFields";
    public static final String FUNCTION_SET_VALUES_FOR_FIELDS = "setValuesForFields";
    public static final String FUNCTION_IMPORT_ANNOTATIONS = "importAnnotations";
    public static final String FUNCTION_MERGE_ANNOTATIONS = "mergeAnnotations";
    public static final String FUNCTION_EXPORT_ANNOTATIONS = "exportAnnotations";
    public static final String FUNCTION_FLATTEN_ANNOTATIONS = "flattenAnnotations";
    public static final String FUNCTION_SET_THEME_MODE = "setThemeMode"; // Hatn
    public static final String FUNCTION_DELETE_ANNOTATIONS = "deleteAnnotations";
    public static final String FUNCTION_SELECT_ANNOTATION = "selectAnnotation";
    public static final String FUNCTION_SET_FLAGS_FOR_ANNOTATIONS = "setFlagsForAnnotations";
    public static final String FUNCTION_SET_PROPERTIES_FOR_ANNOTATION = "setPropertiesForAnnotation";
    public static final String FUNCTION_SET_LEADING_NAV_BUTTON_ICON = "setLeadingNavButtonIcon";
    public static final String FUNCTION_CLOSE_ALL_TABS = "closeAllTabs";
    public static final String FUNCTION_DELETE_ALL_ANNOTATIONS = "deleteAllAnnotations";
    public static final String FUNCTION_GET_PAGE_ROTATION = "getPageRotation";
    public static final String FUNCTION_ROTATE_CLOCKWISE = "rotateClockwise";
    public static final String FUNCTION_ROTATE_COUNTER_CLOCKWISE = "rotateCounterClockwise";
    public static final String FUNCTION_EXPORT_AS_IMAGE = "exportAsImage";
    public static final String FUNCTION_EXPORT_AS_IMAGE_FROM_FILE_PATH = "exportAsImageFromFilePath";
    public static final String FUNCTION_OPEN_ANNOTATION_LIST = "openAnnotationList";
    public static final String FUNCTION_SET_REQUESTED_ORIENTATION = "setRequestedOrientation";
    public static final String FUNCTION_GO_TO_PREVIOUS_PAGE = "gotoPreviousPage";
    public static final String FUNCTION_GO_TO_NEXT_PAGE = "gotoNextPage";
    public static final String FUNCTION_GO_TO_FIRST_PAGE = "gotoFirstPage";
    public static final String FUNCTION_GO_TO_LAST_PAGE = "gotoLastPage";
    public static final String FUNCTION_ADD_BOOKMARK = "addBookmark";
    public static final String FUNCTION_OPEN_BOOKMARK_LIST = "openBookmarkList";
    public static final String FUNCTION_OPEN_OUTLINE_LIST = "openOutlineList";
    public static final String FUNCTION_OPEN_LAYERS_LIST = "openLayersList";
    public static final String FUNCTION_OPEN_THUMBNAILS_VIEW = "openThumbnailsView";
    public static final String FUNCTION_OPEN_ROTATE_DIALOG = "openRotateDialog";
    public static final String FUNCTION_OPEN_ADD_PAGES_VIEW = "openAddPagesView";
    public static final String FUNCTION_OPEN_VIEW_SETTINGS = "openViewSettings";
    public static final String FUNCTION_OPEN_CROP = "openCrop";
    public static final String FUNCTION_OPEN_MANUAL_CROP = "openManualCrop";
    public static final String FUNCTION_OPEN_SEARCH = "openSearch";
    public static final String FUNCTION_OPEN_TAB_SWITCHER = "openTabSwitcher";
    public static final String FUNCTION_OPEN_GO_TO_PAGE_VIEW = "openGoToPageView";
    public static final String FUNCTION_OPEN_NAVIGATION_LISTS = "openNavigationLists";
    public static final String FUNCTION_GET_CURRENT_PAGE = "getCurrentPage";
    public static final String FUNCTION_GROUP_ANNOTATIONS = "groupAnnotations";
    public static final String FUNCTION_UNGROUP_ANNOTATIONS = "ungroupAnnotations";
    public static final String FUNCTION_START_SEARCH_MODE = "startSearchMode";
    public static final String FUNCTION_EXIT_SEARCH_MODE = "exitSearchMode";
    public static final String FUNCTION_ZOOM_WITH_CENTER = "zoomWithCenter";
    public static final String FUNCTION_ZOOM_TO_RECT = "zoomToRect";
    public static final String FUNCTION_GET_ZOOM = "getZoom";
    public static final String FUNCTION_SET_ZOOM_LIMITS = "setZoomLimits";
    public static final String FUNCTION_SMART_ZOOM = "smartZoom";
    public static final String FUNCTION_GET_SAVED_SIGNATURES = "getSavedSignatures";
    public static final String FUNCTION_GET_SAVED_SIGNATURE_FOLDER = "getSavedSignatureFolder";
    public static final String FUNCTION_GET_SAVED_SIGNATURE_JPG_FOLDER = "getSavedSignatureJpgFolder";
    public static final String FUNCTION_SET_BACKGROUND_COLOR = "setBackgroundColor";
    public static final String FUNCTION_SET_DEFAULT_PAGE_COLOR = "setDefaultPageColor";
    public static final String FUNCTION_GET_SCROLL_POS = "getScrollPos";
    public static final String FUNCTION_SET_HORIZONTAL_SCROLL_POSITION = "setHorizontalScrollPosition";
    public static final String FUNCTION_SET_VERTICAL_SCROLL_POSITION = "setVerticalScrollPosition";
    public static final String FUNCTION_GET_VISIBLE_PAGES = "getVisiblePages";

    // Hygen Generated Method Constants
    public static final String FUNCTION_SET_LAYOUT_MODE = "setLayoutMode";
    public static final String FUNCTION_SET_FIT_MODE = "setFitMode";
    public static final String FUNCTION_GET_ANNOTATIONS_ON_PAGE = "getAnnotationsOnPage";

    public static final String BUTTON_TOOLS = "toolsButton";
    public static final String BUTTON_SEARCH = "searchButton";
    public static final String BUTTON_SHARE = "shareButton";
    public static final String BUTTON_VIEW_CONTROLS = "viewControlsButton";
    public static final String BUTTON_THUMBNAILS = "thumbnailsButton";
    public static final String BUTTON_LISTS = "listsButton";
    public static final String BUTTON_THUMBNAIL_SLIDER = "thumbnailSlider";
    public static final String BUTTON_SAVE_COPY = "saveCopyButton";
    public static final String BUTTON_SAVE_IDENTICAL_COPY = "saveIdenticalCopyButton";
    public static final String BUTTON_SAVE_FLATTENED_COPY = "saveFlattenedCopyButton";
    public static final String BUTTON_SAVE_REDUCED_COPY = "saveReducedCopyButton";
    public static final String BUTTON_SAVE_CROPPED_COPY = "saveCroppedCopyButton";
    public static final String BUTTON_SAVE_PASSWORD_COPY = "savePasswordCopyButton";
    public static final String BUTTON_EDIT_PAGES = "editPagesButton";
    public static final String BUTTON_PRINT = "printButton";
    public static final String BUTTON_FILL_AND_SIGN = "fillAndSignButton";
    public static final String BUTTON_PREPARE_FORM = "prepareFormButton";
    public static final String BUTTON_REFLOW_MODE = "reflowModeButton";
    public static final String BUTTON_CLOSE = "closeButton";
    public static final String BUTTON_OUTLINE_LIST = "outlineListButton";
    public static final String BUTTON_ANNOTATION_LIST = "annotationListButton";
    public static final String BUTTON_USER_BOOKMARK_LIST = "userBookmarkListButton";
    public static final String BUTTON_EDIT_MENU = "editMenuButton";
    public static final String BUTTON_CROP_PAGE = "cropPageButton";
    public static final String BUTTON_MORE_ITEMS = "moreItemsButton";
    public static final String BUTTON_UNDO = "undo";
    public static final String BUTTON_REDO = "redo";
    public static final String BUTTON_EDIT_ANNOTATION_TOOLBAR = "editAnnotationToolButton";
    public static final String BUTTON_VIEW_LAYERS = "viewLayersButton";
    public static final String BUTTON_SHOW_FILE_ATTACHMENT = "showFileAttachmentButton";

    public static final String TOOL_BUTTON_FREE_HAND = "freeHandToolButton";
    public static final String TOOL_BUTTON_HIGHLIGHT = "highlightToolButton";
    public static final String TOOL_BUTTON_UNDERLINE = "underlineToolButton";
    public static final String TOOL_BUTTON_SQUIGGLY = "squigglyToolButton";
    public static final String TOOL_BUTTON_STRIKEOUT = "strikeoutToolButton";
    public static final String TOOL_BUTTON_RECTANGLE = "rectangleToolButton";
    public static final String TOOL_BUTTON_ELLIPSE = "ellipseToolButton";
    public static final String TOOL_BUTTON_LINE = "lineToolButton";
    public static final String TOOL_BUTTON_ARROW = "arrowToolButton";
    public static final String TOOL_BUTTON_POLYLINE = "polylineToolButton";
    public static final String TOOL_BUTTON_POLYGON = "polygonToolButton";
    public static final String TOOL_BUTTON_CLOUD = "cloudToolButton";
    public static final String TOOL_BUTTON_SIGNATURE = "signatureToolButton";
    public static final String TOOL_BUTTON_FREE_TEXT = "freeTextToolButton";
    public static final String TOOL_BUTTON_STICKY = "stickyToolButton";
    public static final String TOOL_BUTTON_CALLOUT = "calloutToolButton";
    public static final String TOOL_BUTTON_STAMP = "stampToolButton";

    public static final String TOOL_ANNOTATION_CREATE_FREE_HAND = "AnnotationCreateFreeHand";
    public static final String TOOL_ANNOTATION_CREATE_TEXT_HIGHLIGHT = "AnnotationCreateTextHighlight";
    public static final String TOOL_ANNOTATION_CREATE_TEXT_UNDERLINE = "AnnotationCreateTextUnderline";
    public static final String TOOL_ANNOTATION_CREATE_TEXT_SQUIGGLY = "AnnotationCreateTextSquiggly";
    public static final String TOOL_ANNOTATION_CREATE_TEXT_STRIKEOUT = "AnnotationCreateTextStrikeout";
    public static final String TOOL_ANNOTATION_CREATE_RECTANGLE = "AnnotationCreateRectangle";
    public static final String TOOL_ANNOTATION_CREATE_ELLIPSE = "AnnotationCreateEllipse";
    public static final String TOOL_ANNOTATION_CREATE_LINE = "AnnotationCreateLine";
    public static final String TOOL_ANNOTATION_CREATE_ARROW = "AnnotationCreateArrow";
    public static final String TOOL_ANNOTATION_CREATE_POLYLINE = "AnnotationCreatePolyline";
    public static final String TOOL_ANNOTATION_CREATE_POLYGON = "AnnotationCreatePolygon";
    public static final String TOOL_ANNOTATION_CREATE_POLYGON_CLOUD = "AnnotationCreatePolygonCloud";
    public static final String TOOL_ANNOTATION_CREATE_SIGNATURE = "AnnotationCreateSignature";
    public static final String TOOL_ANNOTATION_CREATE_FREE_TEXT = "AnnotationCreateFreeText";
    public static final String TOOL_ANNOTATION_CREATE_STICKY = "AnnotationCreateSticky";
    public static final String TOOL_ANNOTATION_CREATE_CALLOUT = "AnnotationCreateCallout";
    public static final String TOOL_ANNOTATION_CREATE_STAMP = "AnnotationCreateStamp";
    public static final String TOOL_ANNOTATION_CREATE_DISTANCE_MEASUREMENT = "AnnotationCreateDistanceMeasurement";
    public static final String TOOL_ANNOTATION_CREATE_PERIMETER_MEASUREMENT = "AnnotationCreatePerimeterMeasurement";
    public static final String TOOL_ANNOTATION_CREATE_RECTANGLE_AREA_MEASUREMENT = "AnnotationCreateRectangleAreaMeasurement";
    public static final String TOOL_ANNOTATION_CREATE_AREA_MEASUREMENT = "AnnotationCreateAreaMeasurement";
    public static final String TOOL_ANNOTATION_CREATE_FILE_ATTACHMENT = "AnnotationCreateFileAttachment";
    public static final String TOOL_TEXT_SELECT = "TextSelect";
    public static final String TOOL_ANNOTATION_EDIT = "AnnotationEdit";
    public static final String TOOL_ANNOTATION_CREATE_SOUND = "AnnotationCreateSound";
    public static final String TOOL_ANNOTATION_CREATE_FREE_HIGHLIGHTER = "AnnotationCreateFreeHighlighter";
    public static final String TOOL_ANNOTATION_CREATE_RUBBER_STAMP = "AnnotationCreateRubberStamp";
    public static final String TOOL_ERASER = "Eraser";
    public static final String TOOL_ANNOTATION_CREATE_REDACTION = "AnnotationCreateRedaction";
    public static final String TOOL_ANNOTATION_CREATE_REDACTION_TEXT = "AnnotationCreateRedactionText";
    public static final String TOOL_ANNOTATION_CREATE_LINK = "AnnotationCreateLink";
    public static final String TOOL_ANNOTATION_CREATE_LINK_TEXT = "AnnotationCreateLinkText";
    public static final String TOOL_FORM_CREATE_TEXT_FIELD = "FormCreateTextField";
    public static final String TOOL_FORM_CREATE_CHECKBOX_FIELD = "FormCreateCheckboxField";
    public static final String TOOL_FORM_CREATE_SIGNATURE_FIELD = "FormCreateSignatureField";
    public static final String TOOL_FORM_CREATE_RADIO_FIELD = "FormCreateRadioField";
    public static final String TOOL_FORM_CREATE_COMBO_BOX_FIELD = "FormCreateComboBoxField";
    public static final String TOOL_FORM_CREATE_TOOL_BOX_FIELD = "FormCreateToolBoxField";
    public static final String TOOL_FORM_CREATE_LIST_BOX_FIELD = "FormCreateListBoxField";
    public static final String TOOL_ANNOTATION_SMART_PEN = "AnnotationSmartPen";
    public static final String TOOL_ANNOTATION_LASSO = "AnnotationLasso";

    public static final String ANNOTATION_FLAG_HIDDEN = "hidden";
    public static final String ANNOTATION_FLAG_INVISIBLE = "invisible";
    public static final String ANNOTATION_FLAG_LOCKED = "locked";
    public static final String ANNOTATION_FLAG_LOCKED_CONTENTS = "lockedContents";
    public static final String ANNOTATION_FLAG_NO_ROTATE = "noRotate";
    public static final String ANNOTATION_FLAG_NO_VIEW = "noView";
    public static final String ANNOTATION_FLAG_NO_ZOOM = "noZoom";
    public static final String ANNOTATION_FLAG_PRINT = "print";
    public static final String ANNOTATION_FLAG_READ_ONLY = "readOnly";
    public static final String ANNOTATION_FLAG_TOGGLE_NO_VIEW = "toggleNoView";

    private static final String LAYOUT_MODE_SINGLE = "Single";
    private static final String LAYOUT_MODE_CONTINUOUS = "Continuous";
    private static final String LAYOUT_MODE_FACING = "Facing";
    private static final String LAYOUT_MODE_FACING_CONTINUOUS = "FacingContinuous";
    private static final String LAYOUT_MODE_FACING_OVER = "FacingOver";
    private static final String LAYOUT_MODE_FACING_OVER_CONTINUOUS = "FacingOverContinuous";

    private static final String FIT_MODE_FIT_PAGE = "FitPage";
    private static final String FIT_MODE_FIT_WIDTH = "FitWidth";
    private static final String FIT_MODE_FIT_HEIGHT = "FitHeight";
    private static final String FIT_MODE_ZOOM = "Zoom";

    public static final String THUMBNAIL_FILTER_MODE_ANNOTATED = "annotated";
    public static final String THUMBNAIL_FILTER_MODE_BOOKMARKED = "bookmarked";

    public static final String MENU_ID_STRING_STYLE = "style";
    public static final String MENU_ID_STRING_NOTE = "note";
    public static final String MENU_ID_STRING_COPY = "copy";
    public static final String MENU_ID_STRING_DELETE = "delete";
    public static final String MENU_ID_STRING_FLATTEN = "flatten";
    public static final String MENU_ID_STRING_TEXT = "text";
    public static final String MENU_ID_STRING_EDIT_INK = "editInk";
    public static final String MENU_ID_STRING_SEARCH = "search";
    public static final String MENU_ID_STRING_SHARE = "share";
    public static final String MENU_ID_STRING_MARKUP_TYPE = "markupType";
    public static final String MENU_ID_STRING_SCREEN_CAPTURE = "screenCapture";
    public static final String MENU_ID_STRING_PLAY_SOUND = "playSound";
    public static final String MENU_ID_STRING_OPEN_ATTACHMENT = "openAttachment";
    public static final String MENU_ID_STRING_READ = "read";
    public static final String MENU_ID_STRING_CALIBRATE = "calibrate";
    public static final String MENU_ID_STRING_REDACT = "redact";
    public static final String MENU_ID_STRING_REDACTION = "redaction";
    public static final String MENU_ID_STRING_UNDERLINE = "underline";
    public static final String MENU_ID_STRING_STRIKEOUT = "strikeout";
    public static final String MENU_ID_STRING_SQUIGGLY = "squiggly";
    public static final String MENU_ID_STRING_LINK = "link";
    public static final String MENU_ID_STRING_HIGHLIGHT = "highlight";
    public static final String MENU_ID_STRING_SIGNATURE = "signature";
    public static final String MENU_ID_STRING_RECTANGLE = "rectangle";
    public static final String MENU_ID_STRING_LINE = "line";
    public static final String MENU_ID_STRING_FREE_HAND = "freeHand";
    public static final String MENU_ID_STRING_IMAGE = "image";
    public static final String MENU_ID_STRING_FORM_TEXT = "formText";
    public static final String MENU_ID_STRING_STICKY_NOTE = "stickyNote";
    public static final String MENU_ID_STRING_OVERFLOW = "overflow";
    public static final String MENU_ID_STRING_ERASER = "eraser";
    public static final String MENU_ID_STRING_STAMP = "rubberStamp";
    public static final String MENU_ID_STRING_PAGE_REDACTION = "pageRedaction";
    public static final String MENU_ID_STRING_RECT_REDACTION = "rectRedaction";
    public static final String MENU_ID_STRING_SEARCH_REDACTION = "searchRedaction";
    public static final String MENU_ID_STRING_SHAPE = "shape";
    public static final String MENU_ID_STRING_CLOUD = "cloud";
    public static final String MENU_ID_STRING_POLYGON = "polygon";
    public static final String MENU_ID_STRING_POLYLINE = "polyline";
    public static final String MENU_ID_STRING_FREE_HIGHLIGHTER = "freeHighlighter";
    public static final String MENU_ID_STRING_ARROW = "arrow";
    public static final String MENU_ID_STRING_OVAL = "oval";
    public static final String MENU_ID_STRING_CALLOUT = "callout";
    public static final String MENU_ID_STRING_MEASUREMENT = "measurement";
    public static final String MENU_ID_STRING_AREA_MEASUREMENT = "areaMeasurement";
    public static final String MENU_ID_STRING_PERIMETER_MEASUREMENT = "perimeterMeasurement";
    public static final String MENU_ID_STRING_RECT_AREA_MEASUREMENT = "rectAreaMeasurement";
    public static final String MENU_ID_STRING_RULER = "ruler";
    public static final String MENU_ID_STRING_FORM = "form";
    public static final String MENU_ID_STRING_FORM_COMBO_BOX = "formComboBox";
    public static final String MENU_ID_STRING_FORM_LIST_BOX = "formListBox";
    public static final String MENU_ID_STRING_FORM_CHECK_BOX = "formCheckBox";
    public static final String MENU_ID_STRING_FORM_SIGNATURE = "formSignature";
    public static final String MENU_ID_STRING_FORM_RADIO_GROUP = "formRadioGroup";
    public static final String MENU_ID_STRING_ATTACH = "attach";
    public static final String MENU_ID_STRING_FILE_ATTACHMENT = "fileAttachment";
    public static final String MENU_ID_STRING_SOUND = "sound";
    public static final String MENU_ID_STRING_FREE_TEXT = "freeText";
    public static final String MENU_ID_STRING_CROP = "crop";
    public static final String MENU_ID_STRING_CROP_OK = "crossOK";
    public static final String MENU_ID_STRING_CROP_CANCEL = "crossCancel";
    public static final String MENU_ID_STRING_DEFINE = "define";
    public static final String MENU_ID_STRING_FIELD_SIGNED = "fieldSigned";
    public static final String MENU_ID_STRING_FIRST_ROW_GROUP = "firstRowGroup";
    public static final String MENU_ID_STRING_SECOND_ROW_GROUP = "secondRowGroup";
    public static final String MENU_ID_STRING_GROUP = "group";
    public static final String MENU_ID_STRING_PASTE = "paste";
    public static final String MENU_ID_STRING_RECT_GROUP_SELECT = "rectGroupSelect";
    public static final String MENU_ID_STRING_SIGN_AND_SAVE = "signAndSave";
    public static final String MENU_ID_STRING_THICKNESS = "thickness";
    public static final String MENU_ID_STRING_TRANSLATE = "translate";
    public static final String MENU_ID_STRING_UNGROUP = "ungroup";

    // RGB colors
    public static final String COLOR_RED = "red";
    public static final String COLOR_GREEN = "green";
    public static final String COLOR_BLUE = "blue";

    // Toolbars
    public static final String TAG_VIEW_TOOLBAR = "PDFTron_View";
    public static final String TAG_ANNOTATE_TOOLBAR = "PDFTron_Annotate";
    public static final String TAG_DRAW_TOOLBAR = "PDFTron_Draw";
    public static final String TAG_INSERT_TOOLBAR = "PDFTron_Insert";
    public static final String TAG_FILL_AND_SIGN_TOOLBAR = "PDFTron_Fill_and_Sign";
    public static final String TAG_PREPARE_FORM_TOOLBAR = "PDFTron_Prepare_Form";
    public static final String TAG_MEASURE_TOOLBAR = "PDFTron_Measure";
    public static final String TAG_PENS_TOOLBAR = "PDFTron_Pens";
    public static final String TAG_REDACTION_TOOLBAR = "PDFTron_redact";
    public static final String TAG_FAVORITE_TOOLBAR = "PDFTron_Favorite";

    // Custom toolbars
    public static final String TOOLBAR_KEY_ID = "id";
    public static final String TOOLBAR_KEY_NAME = "name";
    public static final String TOOLBAR_KEY_ICON = "icon";
    public static final String TOOLBAR_KEY_ITEMS = "items";

    // Custom toolbar items
    public static final String TOOLBAR_ITEM_KEY_ID = "id";
    public static final String TOOLBAR_ITEM_KEY_NAME = "name";
    public static final String TOOLBAR_ITEM_KEY_ICON = "icon";

    // View Mode
    public static final String VIEW_MODE_CROP = "viewModeCrop";
    public static final String VIEW_MODE_ROTATION = "viewModeRotation";
    public static final String VIEW_MODE_COLOR_MODE = "viewModeColorMode";
    public static final String VIEW_MODE_VERTICAL_SCROLLING = "viewModeVerticalScrolling";

    // Default Eraser Type
    public static final String DEFAULT_ERASER_TYPE_ANNOTATION = "annotationEraser";
    public static final String DEFAULT_ERASER_TYPE_HYBRID = "hybridEraser";
    public static final String DEFAULT_ERASER_TYPE_INK = "inkEraser";

    // Reflow Orientation
    public static final String REFLOW_ORIENTATION_HORIZONTAL = "horizontal";
    public static final String REFLOW_ORIENTATION_VERTICAL = "vertical";

    // Annotation Manager Edit Mode
    public static final String ANNOTATION_MANAGER_EDIT_MODE_OWN = "editModeOwn";
    public static final String ANNOTATION_MANAGER_EDIT_MODE_ALL = "editModeAll";

    // Annotation Manager Undo Mode
    public static final String ANNOTATION_MANAGER_UNDO_MODE_OWN = "undoModeOwn";
    public static final String ANNOTATION_MANAGER_UNDO_MODE_ALL = "undoModeAll";

    // Scroll direction
    public static final String SCROLL_HORIZONTAL = "horizontal";
    public static final String SCROLL_VERTICAL = "vertical";

    // Navigation List visibility
    public static boolean isBookmarkListVisible = true;
    public static boolean isOutlineListVisible = true;
    public static boolean isAnnotationListVisible = true;

    public static final AtomicInteger toolIdGenerator = new AtomicInteger(1000);
    public static final SparseArray<String> mToolIdMap = new SparseArray<>();

    private static AnnotManager.EditPermissionMode mAnnotationManagerEditMode = AnnotManager.EditPermissionMode.EDIT_OTHERS;
    private static PDFViewCtrl.AnnotationManagerMode mAnnotationManagerUndoMode = PDFViewCtrl.AnnotationManagerMode.ADMIN_UNDO_OTHERS;

    public static class ConfigInfo {
        private int initialPageNumber;
        private boolean isBase64;
        private boolean isDarkMode; // Hatn
        private File tempFile;
        private JSONObject customHeaderJson;
        private Uri fileUri;
        private ArrayList<String> longPressMenuItems;
        private ArrayList<String> longPressMenuOverrideItems;
        private ArrayList<String> hideAnnotationMenuTools;
        private ArrayList<String> annotationMenuItems;
        private ArrayList<String> annotationMenuOverrideItems;
        private boolean autoSaveEnabled;
        private boolean useStylusAsPen;
        private boolean signSignatureFieldWithStamps;
        private boolean showLeadingNavButton;
        private boolean annotationManagerEnabled;
        private String userId;
        private String userName;
        private ArrayList<String> actionOverrideItems;
        private String tabTitle;
        private String openUrlPath;
        private String exportPath;
        private ArrayList<String> appNavRightBarItems;

        public ConfigInfo() {
            this.initialPageNumber = -1;
            this.isBase64 = false;
            this.isDarkMode = false; // Hatn
            this.tempFile = null;
            this.customHeaderJson = null;
            this.fileUri = null;
            this.longPressMenuItems = null;
            this.longPressMenuOverrideItems = null;
            this.hideAnnotationMenuTools = null;
            this.annotationMenuItems = null;
            this.annotationMenuOverrideItems = null;
            this.autoSaveEnabled = true;
            this.useStylusAsPen = true;
            this.signSignatureFieldWithStamps = false;
            this.showLeadingNavButton = true;
            this.actionOverrideItems = null;
            this.tabTitle = null;
            this.openUrlPath = null;
            this.exportPath = null;
            this.annotationManagerEnabled = false;
            this.userId = null;
            this.userName = null;
        }

        public void setInitialPageNumber(int initialPageNumber) {
            this.initialPageNumber = initialPageNumber;
        }

        public void setIsBase64(boolean isBase64) {
            this.isBase64 = isBase64;
        }

        public void setIsDarkMode(boolean isDarkMode) {
            this.isDarkMode = isDarkMode;
        }

        public void setExportPath(String exportPath) {
            this.exportPath = exportPath;
        }

        public void setTempFile(File tempFile) {
            this.tempFile = tempFile;
        }

        public void setCustomHeaderJson(JSONObject customHeaderJson) {
            this.customHeaderJson = customHeaderJson;
        }

        public void setFileUri(Uri fileUri) {
            this.fileUri = fileUri;
        }

        public void setLongPressMenuItems(ArrayList<String> longPressMenuItems) {
            this.longPressMenuItems = longPressMenuItems;
        }

        public void setTopAppNavBarRightBar(ArrayList<String> rightBarItems) {
            this.appNavRightBarItems = rightBarItems;
        }

        public void setLongPressMenuOverrideItems(ArrayList<String> longPressMenuOverrideItems) {
            this.longPressMenuOverrideItems = longPressMenuOverrideItems;
        }

        public void setHideAnnotationMenuTools(ArrayList<String> hideAnnotationMenuTools) {
            this.hideAnnotationMenuTools = hideAnnotationMenuTools;
        }

        public void setAnnotationMenuItems(ArrayList<String> annotationMenuItems) {
            this.annotationMenuItems = annotationMenuItems;
        }

        public void setAnnotationMenuOverrideItems(ArrayList<String> annotationMenuOverrideItems) {
            this.annotationMenuOverrideItems = annotationMenuOverrideItems;
        }

        public void setAutoSaveEnabled(boolean autoSaveEnabled) {
            this.autoSaveEnabled = autoSaveEnabled;
        }

        public void setUseStylusAsPen(boolean useStylusAsPen) {
            this.useStylusAsPen = useStylusAsPen;
        }

        public void setSignSignatureFieldWithStamps(boolean signSignatureFieldWithStamps) {
            this.signSignatureFieldWithStamps = signSignatureFieldWithStamps;
        }

        public void setShowLeadingNavButton(boolean showLeadingNavButton) {
            this.showLeadingNavButton = showLeadingNavButton;
        }

        public void setActionOverrideItems(ArrayList<String> behaviorOverrideItems) {
            this.actionOverrideItems = behaviorOverrideItems;
        }

        public void setTabTitle(String tabTitle) {
            this.tabTitle = tabTitle;
        }

        public void setOpenUrlPath(String openUrlPath) {
            this.openUrlPath = openUrlPath;
        }

        public void setAnnotationManagerEnabled(boolean annotationManagerEnabled) {
            this.annotationManagerEnabled = annotationManagerEnabled;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getInitialPageNumber() {
            return initialPageNumber;
        }

        public boolean isBase64() {
            return isBase64;
        }

        public boolean isDarkMode() { // Hatn
            return isDarkMode;
        }

        public String getExportPath() {
            return exportPath;
        }

        public File getTempFile() {
            return tempFile;
        }

        public JSONObject getCustomHeaderJson() {
            return customHeaderJson;
        }

        public Uri getFileUri() {
            return fileUri;
        }

        public ArrayList<String> getLongPressMenuItems() {
            return longPressMenuItems;
        }

        public ArrayList<String> getLongPressMenuOverrideItems() {
            return longPressMenuOverrideItems;
        }

        public ArrayList<String> getHideAnnotationMenuTools() {
            return hideAnnotationMenuTools;
        }

        public ArrayList<String> getAnnotationMenuItems() {
            return annotationMenuItems;
        }

        public ArrayList<String> getAnnotationMenuOverrideItems() {
            return annotationMenuOverrideItems;
        }

        public boolean isAutoSaveEnabled() {
            return autoSaveEnabled;
        }

        public boolean isUseStylusAsPen() {
            return useStylusAsPen;
        }

        public boolean isSignSignatureFieldWithStamps() {
            return signSignatureFieldWithStamps;
        }

        public boolean isShowLeadingNavButton() {
            return showLeadingNavButton;
        }

        public boolean isAnnotationManagerEnabled() {
            return annotationManagerEnabled;
        }

        public String getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public ArrayList<String> getActionOverrideItems() {
            return actionOverrideItems;
        }

        public String getTabTitle() {
            return tabTitle;
        }

        public String getOpenUrlPath() {
            return openUrlPath;
        }

        public ArrayList<String> getAppNavRightBarItems() {
            return appNavRightBarItems;
        }
    }

    public static ConfigInfo handleOpenDocument(@NonNull ViewerConfig.Builder builder,
            @NonNull ToolManagerBuilder toolManagerBuilder,
            @NonNull PDFViewCtrlConfig pdfViewCtrlConfig, @NonNull String document, @NonNull Context context,
            String configStr) {

        builder
                .maximumTabCount(Integer.MAX_VALUE)
                .multiTabEnabled(false)
                .showCloseTabOption(false)
                .useSupportActionBar(false)
                .skipReadOnlyCheck(true);

        ConfigInfo configInfo = new ConfigInfo();

        toolManagerBuilder.setOpenToolbar(true);
        ArrayList<ToolManager.ToolMode> disabledTools = new ArrayList<>();
        ArrayList<ViewModePickerDialogFragment.ViewModePickerItems> viewModePickerItems = new ArrayList<>();

        boolean isBase64 = false;
        boolean isDarkMode = false;
        String base64FileExtension = null;

        if (configStr != null && !configStr.equals("null")) {
            try {
                JSONObject configJson = new JSONObject(configStr);
                if (!configJson.isNull(KEY_CONFIG_DISABLED_ELEMENTS)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_DISABLED_ELEMENTS);
                    disabledTools.addAll(disableElements(builder, toolManagerBuilder, array));
                }
                if (!configJson.isNull(KEY_CONFIG_DISABLED_TOOLS)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_DISABLED_TOOLS);
                    disabledTools.addAll(disableTools(array));
                }
                if (!configJson.isNull(KEY_CONFIG_MULTI_TAB_ENABLED)) {
                    boolean val = configJson.getBoolean(KEY_CONFIG_MULTI_TAB_ENABLED);
                    builder.multiTabEnabled(val);
                }
                if (!configJson.isNull(KEY_CONFIG_CUSTOM_HEADERS)) {
                    JSONObject customHeaderJson = configJson.getJSONObject(KEY_CONFIG_CUSTOM_HEADERS);
                    configInfo.setCustomHeaderJson(customHeaderJson);
                }
                if (!configJson.isNull(KEY_CONFIG_FIT_MODE)) {
                    String fitString = configJson.getString(KEY_CONFIG_FIT_MODE);
                    PDFViewCtrl.PageViewMode fitMode = convStringToFitMode(fitString);
                    pdfViewCtrlConfig.setPageViewMode(fitMode);
                }
                if (!configJson.isNull(KEY_CONFIG_LAYOUT_MODE)) {
                    String layoutString = configJson.getString(KEY_CONFIG_LAYOUT_MODE);
                    String layoutMode = convStringToLayoutMode(layoutString);
                    PdfViewCtrlSettingsManager.updateViewMode(context, layoutMode);
                }
                if (!configJson.isNull(KEY_CONFIG_TABLET_LAYOUT_ENABLED)) {
                    boolean tabletLayoutEnabled = configJson.getBoolean(KEY_CONFIG_TABLET_LAYOUT_ENABLED);
                    builder.tabletLayoutEnabled(tabletLayoutEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_INITIAL_PAGE_NUMBER)) {
                    int initialPageNumber = configJson.getInt(KEY_CONFIG_INITIAL_PAGE_NUMBER);
                    configInfo.setInitialPageNumber(initialPageNumber);
                }
                if (!configJson.isNull(KEY_CONFIG_IS_BASE_64_STRING)) {
                    isBase64 = configJson.getBoolean(KEY_CONFIG_IS_BASE_64_STRING);
                    configInfo.setIsBase64(isBase64);
                }
                if (!configJson.isNull(KEY_CONFIG_IS_DARK_MODE_STRING)) { // Hatn
                    isDarkMode = configJson.getBoolean(KEY_CONFIG_IS_DARK_MODE_STRING);
                    configInfo.setIsDarkMode(isDarkMode);
                }
                if (!configJson.isNull(KEY_CONFIG_BASE_64_FILE_EXTENSION)) {
                    base64FileExtension = configJson.getString(KEY_CONFIG_BASE_64_FILE_EXTENSION);
                }
                if (!configJson.isNull(KEY_CONFIG_HIDE_THUMBNAIL_FILTER_MODES)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_HIDE_THUMBNAIL_FILTER_MODES);
                    ArrayList<ThumbnailsViewFragment.FilterModes> hideList = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {
                        String filterModeString = array.getString(i);
                        if (filterModeString.equals(THUMBNAIL_FILTER_MODE_ANNOTATED)) {
                            hideList.add(ThumbnailsViewFragment.FilterModes.ANNOTATED);
                        } else if (filterModeString.equals(THUMBNAIL_FILTER_MODE_BOOKMARKED)) {
                            hideList.add(ThumbnailsViewFragment.FilterModes.BOOKMARKED);
                        }
                    }

                    builder.hideThumbnailFilterModes(hideList.toArray(new ThumbnailsViewFragment.FilterModes[0]));
                }
                if (!configJson.isNull(KEY_CONFIG_LONG_PRESS_MENU_ENABLED)) {
                    boolean longPressMenuEnabled = configJson.getBoolean(KEY_CONFIG_LONG_PRESS_MENU_ENABLED);
                    toolManagerBuilder = toolManagerBuilder.setDisableQuickMenu(!longPressMenuEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_LONG_PRESS_MENU_ITEMS)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_LONG_PRESS_MENU_ITEMS);
                    ArrayList<String> longPressMenuItems = convertJSONArrayToArrayList(array);
                    configInfo.setLongPressMenuItems(longPressMenuItems);
                }
                if (!configJson.isNull(KEY_CONFIG_OVERRIDE_LONG_PRESS_MENU_BEHAVIOR)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_OVERRIDE_LONG_PRESS_MENU_BEHAVIOR);
                    ArrayList<String> longPressMenuOverrideItems = convertJSONArrayToArrayList(array);
                    configInfo.setLongPressMenuOverrideItems(longPressMenuOverrideItems);
                }
                if (!configJson.isNull(KEY_CONFIG_HIDE_ANNOTATION_MENU)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_HIDE_ANNOTATION_MENU);
                    ArrayList<String> hideAnnotationMenuTools = convertJSONArrayToArrayList(array);
                    configInfo.setHideAnnotationMenuTools(hideAnnotationMenuTools);
                }
                if (!configJson.isNull(KEY_CONFIG_ANNOTATION_MENU_ITEMS)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_ANNOTATION_MENU_ITEMS);
                    ArrayList<String> annotationMenuItems = convertJSONArrayToArrayList(array);
                    configInfo.setAnnotationMenuItems(annotationMenuItems);
                }
                if (!configJson.isNull(KEY_CONFIG_OVERRIDE_ANNOTATION_MENU_BEHAVIOR)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_OVERRIDE_ANNOTATION_MENU_BEHAVIOR);
                    ArrayList<String> annotationMenuOverrideItems = convertJSONArrayToArrayList(array);
                    configInfo.setAnnotationMenuOverrideItems(annotationMenuOverrideItems);
                }
                if (!configJson.isNull(KEY_CONFIG_EXCLUDED_ANNOTATION_LIST_TYPES)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_EXCLUDED_ANNOTATION_LIST_TYPES);
                    ArrayList<String> excludedTypes = convertJSONArrayToArrayList(array);
                    int[] annotTypes = new int[excludedTypes.size()];
                    for (int i = 0; i < excludedTypes.size(); i++) {
                        String type = excludedTypes.get(i);
                        annotTypes[i] = convStringToAnnotType(type);
                    }

                    builder = builder.excludeAnnotationListTypes(annotTypes);
                }
                if (!configJson.isNull(KEY_CONFIG_EXPORT_PATH)) {
                    String exportPath = configJson.getString(KEY_CONFIG_EXPORT_PATH);
                    configInfo.setExportPath(exportPath);
                } else {
                    String cacheDir = context.getCacheDir().getAbsolutePath();
                    configInfo.setExportPath(cacheDir);
                }
                if (!configJson.isNull(KEY_CONFIG_AUTO_SAVE_ENABLED)) {
                    boolean autoSaveEnabled = configJson.getBoolean(KEY_CONFIG_AUTO_SAVE_ENABLED);
                    configInfo.setAutoSaveEnabled(autoSaveEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_SHOW_DOCUMENT_SAVED_TOAST)) {
                    boolean showDocumentSavedToast = configJson.getBoolean(KEY_CONFIG_SHOW_DOCUMENT_SAVED_TOAST);
                    if (!showDocumentSavedToast) {
                        CommonToast.CommonToastHandler.getInstance().setCommonToastListener(new CommonToast.CommonToastListener() {
                            @Override
                            public boolean canShowToast(int stringRes, @Nullable CharSequence text) {
                                return stringRes != R.string.document_saved_toast_message &&
                                        stringRes != R.string.document_save_error_toast_message;
                            }
                        });
                    }
                }
                if (!configJson.isNull(KEY_CONFIG_PAGE_CHANGE_ON_TAP)) {
                    boolean pageChangeOnTap = configJson.getBoolean(KEY_CONFIG_PAGE_CHANGE_ON_TAP);
                    PdfViewCtrlSettingsManager.setAllowPageChangeOnTap(context, pageChangeOnTap);
                }
                if (!configJson.isNull(KEY_CONFIG_SHOW_SAVED_SIGNATURES)) {
                    boolean showSavedSignatures = configJson.getBoolean(KEY_CONFIG_SHOW_SAVED_SIGNATURES);
                    toolManagerBuilder = toolManagerBuilder.setShowSavedSignatures(showSavedSignatures);
                }
                if (!configJson.isNull(KEY_CONFIG_SIGNATURE_PHOTO_PICKER_ENABLED)) {
                    boolean signaturePhotoPickerEnabled = configJson.getBoolean(KEY_CONFIG_SIGNATURE_PHOTO_PICKER_ENABLED);
                    toolManagerBuilder = toolManagerBuilder.setShowSignatureFromImage(signaturePhotoPickerEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_SIGNATURE_TYPING_ENABLED)) {
                    boolean signatureTypingEnabled = configJson.getBoolean(KEY_CONFIG_SIGNATURE_TYPING_ENABLED);
                    toolManagerBuilder = toolManagerBuilder.setShowTypedSignature(signatureTypingEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_USE_STYLUS_AS_PEN)) {
                    boolean useStylusAsPen = configJson.getBoolean(KEY_CONFIG_USE_STYLUS_AS_PEN);
                    configInfo.setUseStylusAsPen(useStylusAsPen);
                }
                if (!configJson.isNull(KEY_CONFIG_SIGN_SIGNATURE_FIELD_WITH_STAMPS)) {
                    boolean signSignatureFieldWithStamps = configJson.getBoolean(KEY_CONFIG_SIGN_SIGNATURE_FIELD_WITH_STAMPS);
                    configInfo.setSignSignatureFieldWithStamps(signSignatureFieldWithStamps);
                }
                if (!configJson.isNull(KEY_CONFIG_SIGNATURE_COLORS)) {
                    JSONArray signatureColors = configJson.getJSONArray(KEY_CONFIG_SIGNATURE_COLORS);
                    toolManagerBuilder = setSignatureColors(signatureColors, toolManagerBuilder);
                }
                if (!configJson.isNull(KEY_CONFIG_SELECT_ANNOTATION_AFTER_CREATION)) {
                    boolean selectAnnotationAfterCreation = configJson.getBoolean(KEY_CONFIG_SELECT_ANNOTATION_AFTER_CREATION);
                    toolManagerBuilder.setAutoSelect(selectAnnotationAfterCreation);
                }
                if (!configJson.isNull(KEY_CONFIG_PAGE_INDICATOR_ENABLED)) {
                    boolean pageIndicatorEnabled = configJson.getBoolean(KEY_CONFIG_PAGE_INDICATOR_ENABLED);
                    builder.showPageNumberIndicator(pageIndicatorEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_SHOW_QUICK_NAVIGATION_BUTTON)) {
                    boolean showQuickNavButton = configJson.getBoolean(KEY_CONFIG_SHOW_QUICK_NAVIGATION_BUTTON);
                    builder.pageStackEnabled(showQuickNavButton);
                }
                if (!configJson.isNull(KEY_CONFIG_FOLLOW_SYSTEM_DARK_MODE)) {
                    boolean followSystem = configJson.getBoolean(KEY_CONFIG_FOLLOW_SYSTEM_DARK_MODE);
                    PdfViewCtrlSettingsManager.setFollowSystemDarkMode(context, followSystem);
                }
                if (!configJson.isNull(KEY_CONFIG_DOWNLOAD_DIALOG_ENABLED)) {
                    boolean downloadDialogEnabled = configJson.getBoolean(KEY_CONFIG_DOWNLOAD_DIALOG_ENABLED);
                    builder.showDownloadDialog(downloadDialogEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_SINGLE_LINE_TOOLBAR)) {
                    boolean singleLineToolbar = configJson.getBoolean(KEY_CONFIG_SINGLE_LINE_TOOLBAR);
                    builder.useCompactViewer(singleLineToolbar);
                }
                if (!configJson.isNull(KEY_CONFIG_ANNOTATION_TOOLBARS)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_ANNOTATION_TOOLBARS);
                    setAnnotationBars(array, builder, context);
                }
                if (!configJson.isNull(KEY_CONFIG_HIDE_DEFAULT_ANNOTATION_TOOLBARS)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_HIDE_DEFAULT_ANNOTATION_TOOLBARS);
                    ArrayList<String> tagList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        String tag = array.getString(i);
                        if (!Utils.isNullOrEmpty(tag)) {
                            tagList.add(tag);
                        }
                    }
                    builder.hideToolbars(tagList.toArray(new String[tagList.size()]));
                }
                if (!configJson.isNull(KEY_CONFIG_HIDE_ANNOTATION_TOOLBAR_SWITCHER)) {
                    boolean hideAnnotationToolbarSwitcher = configJson.getBoolean(KEY_CONFIG_HIDE_ANNOTATION_TOOLBAR_SWITCHER);
                    builder.showToolbarSwitcher(!hideAnnotationToolbarSwitcher);
                }
                if (!configJson.isNull(KEY_CONFIG_INITIAL_TOOLBAR)) {
                    String initialToolbar = configJson.getString(KEY_CONFIG_INITIAL_TOOLBAR);
                    if (!initialToolbar.isEmpty()) {
                        builder.initialToolbarTag(initialToolbar).rememberLastUsedToolbar(false);
                    }
                }
                if (!configJson.isNull(KEY_CONFIG_HIDE_TOP_TOOLBARS)) {
                    boolean hideTopToolbars = configJson.getBoolean(KEY_CONFIG_HIDE_TOP_TOOLBARS);
                    builder.showAppBar(!hideTopToolbars);
                }
                if (!configJson.isNull(KEY_CONFIG_HIDE_TOOLBARS_ON_TAP)) {
                    boolean hideToolbarsOnTap = configJson.getBoolean(KEY_CONFIG_HIDE_TOOLBARS_ON_TAP);
                    builder.permanentToolbars(!hideToolbarsOnTap);
                }
                if (!configJson.isNull(KEY_CONFIG_HIDE_TOP_APP_NAV_BAR)) {
                    boolean hideTopAppNavBars = configJson.getBoolean(KEY_CONFIG_HIDE_TOP_APP_NAV_BAR);
                    builder.showTopToolbar(!hideTopAppNavBars);
                }
                if (!configJson.isNull(KEY_CONFIG_HIDE_BOTTOM_TOOLBAR)) {
                    boolean hideBottomToolbar = configJson.getBoolean(KEY_CONFIG_HIDE_BOTTOM_TOOLBAR);
                    builder.showBottomToolbar(!hideBottomToolbar);
                }
                if (!configJson.isNull(KEY_CONFIG_HIDE_PRESET_BAR)) {
                    boolean hidePresetBar = configJson.getBoolean(KEY_CONFIG_HIDE_PRESET_BAR);
                    builder.hidePresetBar(hidePresetBar);
                }
                if (!configJson.isNull(KEY_CONFIG_BOTTOM_TOOLBAR)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_BOTTOM_TOOLBAR);
                    setBottomToolbar(array, builder);
                }
                if (!configJson.isNull(KEY_CONFIG_SHOW_LEADING_NAV_BUTTON)) {
                    boolean showLeadingNavButton = configJson.getBoolean(KEY_CONFIG_SHOW_LEADING_NAV_BUTTON);
                    configInfo.setShowLeadingNavButton(showLeadingNavButton);
                }
                if (!configJson.isNull(KEY_CONFIG_DOCUMENT_SLIDER_ENABLED)) {
                    boolean documentSliderEnabled = configJson.getBoolean(KEY_CONFIG_DOCUMENT_SLIDER_ENABLED);
                    builder.showDocumentSlider(documentSliderEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_REMEMBER_LAST_USED_TOOL)) {
                    boolean rememberLastUsedTool = configJson.getBoolean(KEY_CONFIG_REMEMBER_LAST_USED_TOOL);
                    builder.rememberLastUsedTool(rememberLastUsedTool);
                }
                if (!configJson.isNull(KEY_CONFIG_READ_ONLY)) {
                    boolean readOnly = configJson.getBoolean(KEY_CONFIG_READ_ONLY);
                    builder.documentEditingEnabled(!readOnly);
                    if (readOnly) {
                        builder.skipReadOnlyCheck(false);
                    }
                }
                if (!configJson.isNull(KEY_CONFIG_THUMBNAIL_VIEW_EDITING_ENABLED)) {
                    boolean thumbnailViewEditingEnabled = configJson.getBoolean(KEY_CONFIG_THUMBNAIL_VIEW_EDITING_ENABLED);
                    builder.thumbnailViewEditingEnabled(thumbnailViewEditingEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_ANNOTATION_AUTHOR)) {
                    String annotationAuthor = configJson.getString(KEY_CONFIG_ANNOTATION_AUTHOR);
                    if (!annotationAuthor.isEmpty()) {
                        PdfViewCtrlSettingsManager.updateAuthorName(context, annotationAuthor);
                        PdfViewCtrlSettingsManager.setAnnotListShowAuthor(context, true);
                    }
                }
                if (!configJson.isNull(KEY_CONFIG_CONTINUOUS_ANNOTATION_EDITING)) {
                    boolean continuousAnnotationEditing = configJson.getBoolean(KEY_CONFIG_CONTINUOUS_ANNOTATION_EDITING);
                    PdfViewCtrlSettingsManager.setContinuousAnnotationEdit(context, continuousAnnotationEditing);
                }
                if (!configJson.isNull(KEY_CONFIG_ANNOTATION_PERMISSION_CHECK_ENABLED)) {
                    boolean annotationPermissionCheckEnabled = configJson.getBoolean(KEY_CONFIG_ANNOTATION_PERMISSION_CHECK_ENABLED);
                    toolManagerBuilder = toolManagerBuilder.setAnnotPermission(annotationPermissionCheckEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_ANNOTATIONS_LIST_EDITING_ENABLED)) {
                    boolean annotationsListEditingEnabled = configJson.getBoolean(KEY_CONFIG_ANNOTATIONS_LIST_EDITING_ENABLED);
                    builder.annotationsListEditingEnabled(annotationsListEditingEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_USER_BOOKMARKS_LIST_EDITING_ENABLED)) {
                    boolean userBookmarksListEditingEnabled = configJson.getBoolean(KEY_CONFIG_USER_BOOKMARKS_LIST_EDITING_ENABLED);
                    builder.userBookmarksListEditingEnabled(userBookmarksListEditingEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_OUTLINE_LIST_EDITING_ENABLED)) {
                    boolean outlineListEditingEnabled = configJson.getBoolean(KEY_CONFIG_OUTLINE_LIST_EDITING_ENABLED);
                    builder.outlineListEditingEnabled(outlineListEditingEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_SHOW_NAVIGATION_LIST_AS_SIDE_PANEL_ON_LARGE_DEVICES)) {
                    boolean showNavigationListAsSidePanelOnLargeDevices = configJson.getBoolean(KEY_CONFIG_SHOW_NAVIGATION_LIST_AS_SIDE_PANEL_ON_LARGE_DEVICES);
                    builder.navigationListAsSheetOnLargeDevice(showNavigationListAsSidePanelOnLargeDevices);
                }
                if (!configJson.isNull(KEY_CONFIG_OVERRIDE_BEHAVIOR)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_OVERRIDE_BEHAVIOR);
                    ArrayList<String> actionOverrideItems = convertJSONArrayToArrayList(array);
                    configInfo.setActionOverrideItems(actionOverrideItems);
                }
                if (!configJson.isNull(KEY_CONFIG_TAB_TITLE)) {
                    String tabTitle = configJson.getString(KEY_CONFIG_TAB_TITLE);
                    configInfo.setTabTitle(tabTitle);
                }
                if (!configJson.isNull(KEY_CONFIG_PERMANENT_PAGE_NUMBER_INDICATOR)) {
                    boolean permanentPageNumberIndicator = configJson.getBoolean(KEY_CONFIG_PERMANENT_PAGE_NUMBER_INDICATOR);
                    builder.permanentPageNumberIndicator(permanentPageNumberIndicator);
                }
                if (!configJson.isNull(KEY_CONFIG_OPEN_SAVED_COPY_IN_NEW_TAB)) {
                    boolean openSavedCopyInNewTab = configJson.getBoolean(KEY_CONFIG_OPEN_SAVED_COPY_IN_NEW_TAB);
                    builder.openSavedCopyInNewTab(openSavedCopyInNewTab);
                }
                if (!configJson.isNull(KEY_CONFIG_MAX_TAB_COUNT)) {
                    int maxTabCount = configJson.getInt(KEY_CONFIG_MAX_TAB_COUNT);
                    builder.maximumTabCount(maxTabCount);
                }
                if (!configJson.isNull(KEY_CONFIG_OPEN_URL_PATH)) {
                    String openUrlPath = configJson.getString(KEY_CONFIG_OPEN_URL_PATH);
                    configInfo.setOpenUrlPath(openUrlPath);
                } else {
                    String cacheDir = context.getCacheDir().getAbsolutePath();
                    configInfo.setOpenUrlPath(cacheDir);
                }
                if (!configJson.isNull(KEY_CONFIG_DISABLE_EDITING_BY_ANNOTATION_TYPE)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_DISABLE_EDITING_BY_ANNOTATION_TYPE);
                    ArrayList<String> items = convertJSONArrayToArrayList(array);
                    int[] annotTypes = new int[items.size()];
                    for (int i = 0; i < items.size(); i++) {
                        annotTypes[i] = convStringToAnnotType(items.get(i));
                    }
                    toolManagerBuilder.disableAnnotEditing(annotTypes);
                }
                if (!configJson.isNull(KEY_CONFIG_ANNOTATIONS_LIST_FILTER_ENABLED)) {
                    boolean annotationsListFilterEnabled = configJson.getBoolean(KEY_CONFIG_ANNOTATIONS_LIST_FILTER_ENABLED);
                    builder.annotationsListFilterEnabled(annotationsListFilterEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_HIDE_VIEW_MODE_ITEMS)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_HIDE_VIEW_MODE_ITEMS);
                    for (int i = 0; i < array.length(); i++) {
                        if (VIEW_MODE_COLOR_MODE.equals(array.getString(i))) {
                            viewModePickerItems.add(ViewModePickerDialogFragment.ViewModePickerItems.ITEM_ID_COLORMODE);
                        }
                        if (VIEW_MODE_CROP.equals(array.getString(i))) {
                            viewModePickerItems.add(ViewModePickerDialogFragment.ViewModePickerItems.ITEM_ID_USERCROP);
                        }
                        if (VIEW_MODE_ROTATION.equals(array.getString(i))) {
                            viewModePickerItems.add(ViewModePickerDialogFragment.ViewModePickerItems.ITEM_ID_ROTATION);
                        }
                        if (VIEW_MODE_VERTICAL_SCROLLING.equals(array.getString(i))) {
                            viewModePickerItems
                                    .add(ViewModePickerDialogFragment.ViewModePickerItems.ITEM_ID_CONTINUOUS);
                        }
                    }
                }
                if (!configJson.isNull(KEY_CONFIG_DEFAULT_ERASER_TYPE)) {
                    String eraserType = configJson.getString(KEY_CONFIG_DEFAULT_ERASER_TYPE);
                    if (DEFAULT_ERASER_TYPE_ANNOTATION.equals(eraserType)) {
                        toolManagerBuilder = toolManagerBuilder.setEraserType(Eraser.EraserType.ANNOTATION_ERASER);
                    } else if (DEFAULT_ERASER_TYPE_HYBRID.equals(eraserType)) {
                        toolManagerBuilder = toolManagerBuilder.setEraserType(Eraser.EraserType.HYBRID_ERASER);
                    } else if (DEFAULT_ERASER_TYPE_INK.equals(eraserType)) {
                        toolManagerBuilder = toolManagerBuilder.setEraserType(Eraser.EraserType.INK_ERASER);
                    }
                }
                if (!configJson.isNull(KEY_CONFIG_AUTO_RESIZE_FREE_TEXT_ENABLED)) {
                    boolean autoResizeFreeTextEnabled = configJson.getBoolean(KEY_CONFIG_AUTO_RESIZE_FREE_TEXT_ENABLED);
                    toolManagerBuilder = toolManagerBuilder.setAutoResizeFreeText(autoResizeFreeTextEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_RESTRICT_DOWNLOAD_USAGE)) {
                    boolean restrictDownloadUsage = configJson.getBoolean(KEY_CONFIG_RESTRICT_DOWNLOAD_USAGE);
                    builder.restrictDownloadUsage(restrictDownloadUsage);
                }
                if (!configJson.isNull(KEY_CONFIG_REFLOW_ORIENTATION)) {
                    int orientation = ReflowControl.HORIZONTAL;
                    String reflowOrientation = configJson.getString(KEY_CONFIG_REFLOW_ORIENTATION);
                    if (REFLOW_ORIENTATION_VERTICAL.equals(reflowOrientation)) {
                        orientation = ReflowControl.VERTICAL;
                    }
                    builder.reflowOrientation(orientation);
                }
                if (!configJson.isNull(KEY_CONFIG_IMAGE_IN_REFLOW_MODE_ENABLED)) {
                    boolean imageInReflowModeEnabled = configJson.getBoolean(KEY_CONFIG_IMAGE_IN_REFLOW_MODE_ENABLED);
                    builder.imageInReflowEnabled(imageInReflowModeEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_ANNOTATION_MANAGER_ENABLED)) {
                    boolean annotationManagerEnabled = configJson.getBoolean(KEY_CONFIG_ANNOTATION_MANAGER_ENABLED);
                    configInfo.setAnnotationManagerEnabled(annotationManagerEnabled);
                }
                if (!configJson.isNull(KEY_CONFIG_USER_ID)) {
                    String userId = configJson.getString(KEY_CONFIG_USER_ID);
                    if (!userId.isEmpty()) {
                        configInfo.setUserId(userId);
                    }
                }
                if (!configJson.isNull(KEY_CONFIG_USER_NAME)) {
                    String userName = configJson.getString(KEY_CONFIG_USER_NAME);
                    if (!userName.isEmpty()) {
                        configInfo.setUserName(userName);
                    }
                }
                if (!configJson.isNull(KEY_CONFIG_ANNOTATION_MANAGER_EDIT_MODE)) {
                    String editMode = configJson.getString(KEY_CONFIG_ANNOTATION_MANAGER_EDIT_MODE);
                    if (ANNOTATION_MANAGER_EDIT_MODE_OWN.equals(editMode)) {
                        mAnnotationManagerEditMode = AnnotManager.EditPermissionMode.EDIT_OWN;
                    }
                }
                if (!configJson.isNull(KEY_CONFIG_ANNOTATION_MANAGER_UNDO_MODE)) {
                    String undoMode = configJson.getString(KEY_CONFIG_ANNOTATION_MANAGER_UNDO_MODE);
                    if (ANNOTATION_MANAGER_UNDO_MODE_OWN.equals(undoMode)) {
                        mAnnotationManagerUndoMode = PDFViewCtrl.AnnotationManagerMode.ADMIN_UNDO_OWN;
                    }
                }
                if (!configJson.isNull(KEY_CONFIG_ANNOTATION_TOOLBAR_GRAVITY)) {
                    String gravityStr = configJson.getString(KEY_CONFIG_ANNOTATION_TOOLBAR_GRAVITY);
                    int gravity = Gravity.END;
                    if (KEY_GRAVITY_START.equals(gravityStr)) {
                        gravity = Gravity.START;
                    }
                    builder.toolbarItemGravity(gravity);
                }
                if (!configJson.isNull(KEY_CONFIG_QUICK_BOOKMARK_CREATION)) {
                    Boolean quickBookmark = configJson.getBoolean(KEY_CONFIG_QUICK_BOOKMARK_CREATION);
                    builder.quickBookmarkCreation(quickBookmark);
                }
                if (!configJson.isNull(KEY_CONFIG_FULL_SCREEN_MODE_ENABLED)) {
                    boolean fullScreenMode = configJson.getBoolean(KEY_CONFIG_FULL_SCREEN_MODE_ENABLED);
                    PdfViewCtrlSettingsManager.setFullScreenMode(context, fullScreenMode);
                    builder.fullscreenModeEnabled(fullScreenMode);
                } else {
                    PdfViewCtrlSettingsManager.setFullScreenMode(context, false);
                    builder.fullscreenModeEnabled(false);
                }
                if (!configJson.isNull(KEY_CONFIG_TOP_APP_NAV_BAR_RIGHT_BAR)) {
                    JSONArray array = configJson.getJSONArray(KEY_CONFIG_TOP_APP_NAV_BAR_RIGHT_BAR);
                    ArrayList<String> navBarRightBarItems = convertJSONArrayToArrayList(array);
                    configInfo.setTopAppNavBarRightBar(navBarRightBarItems);
                }
                // Hygen Generated Configs
                if (!configJson.isNull(KEY_CONFIG_MAX_SIGNATURE_COUNT)) {
                    SignatureDialogFragment.MAX_SIGNATURES = configJson.getInt(KEY_CONFIG_MAX_SIGNATURE_COUNT);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        final Uri fileUri = getUri(context, document, isBase64, base64FileExtension);
        configInfo.setFileUri(fileUri);

        if (fileUri != null) {
            builder.openUrlCachePath(configInfo.getOpenUrlPath())
                    .saveCopyExportPath(configInfo.getExportPath());
            if (disabledTools.size() > 0) {
                ToolManager.ToolMode[] modes = disabledTools.toArray(new ToolManager.ToolMode[0]);
                if (modes.length > 0) {
                    toolManagerBuilder.disableToolModes(modes);
                }
            }

            if (viewModePickerItems.size() > 0) {
                builder = builder.hideViewModeItems(viewModePickerItems.toArray(new ViewModePickerDialogFragment.ViewModePickerItems[0]));
            }

            if (isBase64 && fileUri.getPath() != null) {
                File tempFile = new File(fileUri.getPath());
                configInfo.setTempFile(tempFile);
            }
        }

        builder.pdfViewCtrlConfig(pdfViewCtrlConfig)
                .toolManagerBuilder(toolManagerBuilder);
        return configInfo;
    }

    private static void setAnnotationBars(JSONArray array, ViewerConfig.Builder builder, Context context) throws JSONException {
        if (context == null) {
            return;
        }
        for (int i = 0; i < array.length(); i++) {
            Object annotationBar = array.get(i);
            if (annotationBar instanceof String) {
                String tag = (String) annotationBar;
                if (isValidToolbarTag(tag)) {
                    builder = builder.addToolbarBuilder(
                            DefaultToolbars.getDefaultAnnotationToolbarBuilderByTag(tag)
                    );
                }
            } else if (annotationBar instanceof JSONObject) {
                JSONObject object = (JSONObject) annotationBar;
                String toolbarId = null, toolbarName = null, toolbarIcon = null;
                JSONArray toolbarItems = null;

                if (!object.isNull(TOOLBAR_KEY_ID)) {
                    toolbarId = object.getString(TOOLBAR_KEY_ID);
                }
                if (!object.isNull(TOOLBAR_KEY_NAME)) {
                    toolbarName = object.getString(TOOLBAR_KEY_NAME);
                }
                if (!object.isNull(TOOLBAR_KEY_ICON)) {
                    toolbarIcon = object.getString(TOOLBAR_KEY_ICON);
                }
                if (!object.isNull(TOOLBAR_KEY_ITEMS)) {
                    toolbarItems = getJSONArrayFromJSONObject(object, TOOLBAR_KEY_ITEMS);
                }

                if (!Utils.isNullOrEmpty(toolbarId) && !Utils.isNullOrEmpty(toolbarName)
                        && toolbarItems != null && toolbarItems.length() > 0) {
                    AnnotationToolbarBuilder toolbarBuilder = AnnotationToolbarBuilder.withTag(toolbarId)
                            .setToolbarName(toolbarName)
                            .setIcon(convStringToToolbarDefaultIconRes(toolbarIcon));
                    for (int j = 0; j < toolbarItems.length(); j++) {
                        Object type = toolbarItems.get(j);
                        if (type instanceof String) {
                            String toolStr = toolbarItems.getString(j);
                            ToolbarButtonType buttonType = convStringToToolbarType(toolStr);
                            int buttonId = convStringToButtonId(toolStr);
                            if (buttonType != null && buttonId != 0) {
                                if (buttonType == ToolbarButtonType.UNDO ||
                                        buttonType == ToolbarButtonType.REDO) {
                                    toolbarBuilder.addToolStickyButton(buttonType, buttonId);
                                } else {
                                    toolbarBuilder.addToolButton(buttonType, buttonId);
                                }
                            }
                        } else if (type instanceof JSONObject) {
                            // custom buttons
                            JSONObject item = toolbarItems.getJSONObject(j);
                            String itemId = null, itemName = null, itemIcon = null;
                            if (!item.isNull(TOOLBAR_KEY_ID)) {
                                itemId = item.getString(TOOLBAR_KEY_ID);
                            }
                            if (!item.isNull(TOOLBAR_KEY_NAME)) {
                                itemName = item.getString(TOOLBAR_KEY_NAME);
                            }
                            if (!item.isNull(TOOLBAR_KEY_ICON)) {
                                itemIcon = item.getString(TOOLBAR_KEY_ICON);
                            }

                            if (!Utils.isNullOrEmpty(itemId) && itemName != null && !Utils.isNullOrEmpty(itemIcon)) {
                                int res = Utils.getResourceDrawable(context, itemIcon);
                                if (res != 0) {
                                    int id = toolIdGenerator.getAndIncrement();
                                    mToolIdMap.put(id, itemId);
                                    toolbarBuilder.addCustomButton(itemName, res, id);
                                }
                            }
                        }
                    }
                    builder = builder.addToolbarBuilder(toolbarBuilder);
                }
            }
        }
    }

    private static void setBottomToolbar(JSONArray array, ViewerConfig.Builder builder) throws JSONException {
        BottomBarBuilder customBottomBar = BottomBarBuilder.withTag("CustomBottomBar");

        for (int i = 0; i < array.length(); i++) {
            String item = array.getString(i);

            if (BUTTON_THUMBNAILS.equals(item)) {
                customBottomBar.addCustomButton(R.string.pref_viewmode_thumbnails, R.drawable.ic_thumbnails_grid_black_24dp, R.id.action_thumbnails);
            } else if (BUTTON_LISTS.equals(item)) {
                customBottomBar.addCustomButton(R.string.action_outline, R.drawable.ic_outline_white_24dp, R.id.action_outline);
            } else if (BUTTON_SHARE.equals(item)) {
                customBottomBar.addCustomButton(R.string.action_file_share, R.drawable.ic_share_black_24dp, R.id.action_share);
            } else if (BUTTON_VIEW_CONTROLS.equals(item)) {
                customBottomBar.addCustomButton(R.string.action_view_mode, R.drawable.ic_viewing_mode_white_24dp, R.id.action_viewmode);
            } else if (BUTTON_SEARCH.equals(item)) {
                customBottomBar.addCustomButton(R.string.action_search, R.drawable.ic_search_white_24dp, R.id.action_search);
            } else if (BUTTON_REFLOW_MODE.equals(item)) {
                customBottomBar.addCustomButton(R.string.pref_viewmode_reflow, R.drawable.ic_view_mode_reflow_black_24dp, R.id.action_reflow_mode);
            }
        }

        builder.bottomBarBuilder(customBottomBar);
    }

    private static ToolManagerBuilder setSignatureColors(JSONArray array, ToolManagerBuilder toolManagerBuilder) throws JSONException {
        int[] signatureColors = new int[array.length()];

        for (int i = 0; i < array.length(); i++) {
            JSONObject color = array.getJSONObject(i);

            if (color != null) {
                int red = color.getInt(COLOR_RED);
                int green = color.getInt(COLOR_GREEN);
                int blue = color.getInt(COLOR_BLUE);

                signatureColors[i] = Color.rgb(red, green, blue);
            }
        }

        return toolManagerBuilder.setSignatureColors(signatureColors);
    }

    private static Uri getUri(Context context, String path, boolean isBase64, String base64FileExtension) {
        if (context == null || path == null) {
            return null;
        }
        try {
            if (isBase64) {
                byte[] data = Base64.decode(path, Base64.DEFAULT);
                File tempFile = File.createTempFile("tmp", base64FileExtension == null ? ".pdf" : base64FileExtension);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(tempFile);
                    IOUtils.write(data, fos);
                    return Uri.fromFile(tempFile);
                } finally {
                    IOUtils.closeQuietly(fos);
                }
            }
            Uri fileUri = Uri.parse(path);
            if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(fileUri.getScheme())) {
                String resNameWithExtension = fileUri.getLastPathSegment();
                String extension = FilenameUtils.getExtension(resNameWithExtension);
                String resName = FilenameUtils.removeExtension(resNameWithExtension);
                int resId = Utils.getResourceRaw(context, resName);
                if (resId != 0) {
                    File file = Utils.copyResourceToLocal(context, resId,
                            resName, "." + extension);
                    if (null != file && file.exists()) {
                        fileUri = Uri.fromFile(file);
                    }
                }
            } else if (ContentResolver.SCHEME_FILE.equals(fileUri.getScheme())) {
                File file = new File(fileUri.getPath());
                fileUri = Uri.fromFile(file);
            }
            return fileUri;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static ArrayList<ToolManager.ToolMode> disableElements(ViewerConfig.Builder builder, ToolManagerBuilder toolManagerBuilder, JSONArray args) throws JSONException {

        ArrayList<ViewModePickerDialogFragment.ViewModePickerItems> viewModePickerItems = new ArrayList<>();
        ArrayList<Integer> saveCopyOptions = new ArrayList<>();

        for (int i = 0; i < args.length(); i++) {
            String item = args.getString(i);
            if (BUTTON_TOOLS.equals(item)) {
                builder = builder.showAnnotationToolbarOption(false);
            } else if (BUTTON_SEARCH.equals(item)) {
                builder = builder.showSearchView(false);
            } else if (BUTTON_SHARE.equals(item)) {
                builder = builder.showShareOption(false);
            } else if (BUTTON_VIEW_CONTROLS.equals(item)) {
                builder = builder.showDocumentSettingsOption(false);
            } else if (BUTTON_THUMBNAILS.equals(item)) {
                builder = builder.showThumbnailView(false);
            } else if (BUTTON_LISTS.equals(item)) {
                builder = builder
                        .showAnnotationsList(false)
                        .showOutlineList(false)
                        .showUserBookmarksList(false);
                isBookmarkListVisible = false;
                isOutlineListVisible = false;
                isAnnotationListVisible = false;
            } else if (BUTTON_THUMBNAIL_SLIDER.equals(item)) {
                builder = builder.showBottomNavBar(false);
            } else if (BUTTON_SAVE_COPY.equals(item)) {
                builder = builder.showSaveCopyOption(false);
            } else if (BUTTON_SAVE_IDENTICAL_COPY.equals(item)) {
                saveCopyOptions.add(R.id.menu_export_copy);
            } else if (BUTTON_SAVE_FLATTENED_COPY.equals(item)) {
                saveCopyOptions.add(R.id.menu_export_flattened_copy);
            } else if (BUTTON_SAVE_REDUCED_COPY.equals(item)) {
                saveCopyOptions.add(R.id.menu_export_optimized_copy);
            } else if (BUTTON_SAVE_CROPPED_COPY.equals(item)) {
                saveCopyOptions.add(R.id.menu_export_cropped_copy);
            } else if (BUTTON_SAVE_PASSWORD_COPY.equals(item)) {
                saveCopyOptions.add(R.id.menu_export_password_copy);
            } else if (BUTTON_EDIT_PAGES.equals(item)) {
                builder = builder.showEditPagesOption(false);
            } else if (BUTTON_PRINT.equals(item)) {
                builder = builder.showPrintOption(false);
            } else if (BUTTON_FILL_AND_SIGN.equals(item)) {
                builder = builder.showFillAndSignToolbarOption(false);
            } else if (BUTTON_PREPARE_FORM.equals(item)) {
                builder = builder.showFormToolbarOption(false);
            } else if (BUTTON_REFLOW_MODE.equals(item)) {
                builder = builder.showReflowOption(false);
                viewModePickerItems.add(ViewModePickerDialogFragment.ViewModePickerItems.ITEM_ID_REFLOW);
            } else if (BUTTON_CLOSE.equals(item)) {
                builder = builder.showCloseTabOption(false);
            } else if (BUTTON_OUTLINE_LIST.equals(item)) {
                builder = builder.showOutlineList(false);
                isOutlineListVisible = false;
            } else if (BUTTON_ANNOTATION_LIST.equals(item)) {
                builder = builder.showAnnotationsList(false);
                isAnnotationListVisible = false;
            } else if (BUTTON_USER_BOOKMARK_LIST.equals(item)) {
                builder = builder.showUserBookmarksList(false);
                isBookmarkListVisible = false;
            } else if (BUTTON_EDIT_MENU.equals(item)) {
                builder = builder.showEditMenuOption(false);
            } else if (BUTTON_CROP_PAGE.equals(item)) {
                viewModePickerItems.add(ViewModePickerDialogFragment.ViewModePickerItems.ITEM_ID_USERCROP);
            } else if (BUTTON_UNDO.equals(item)) {
                toolManagerBuilder.setShowUndoRedo(false);
            } else if (BUTTON_REDO.equals(item)) {
                toolManagerBuilder.setShowUndoRedo(false);
            } else if (BUTTON_MORE_ITEMS.equals(item)) {
                builder = builder
                        .showEditPagesOption(false)
                        .showPrintOption(false)
                        .showCloseTabOption(false)
                        .showSaveCopyOption(false)
                        .showFormToolbarOption(false)
                        .showFillAndSignToolbarOption(false)
                        .showEditMenuOption(false)
                        .showReflowOption(false);
            } else if (BUTTON_VIEW_LAYERS.equals(item)) {
                builder = builder.showViewLayersToolbarOption(false);
            } else if (BUTTON_SHOW_FILE_ATTACHMENT.equals(item)) {
                builder = builder.showFileAttachmentOption(false);
            }
        }

        if (!saveCopyOptions.isEmpty()) {
            int[] modes = new int[saveCopyOptions.size()];
            for (int j = 0; j < modes.length; j++) {
                modes[j] = saveCopyOptions.get(j);
            }
            builder.hideSaveCopyOptions(modes);
        }

        builder.hideViewModeItems(viewModePickerItems.toArray(new ViewModePickerDialogFragment.ViewModePickerItems[0]));
        return disableTools(args);
    }

    private static ArrayList<ToolManager.ToolMode> disableTools(JSONArray args) throws JSONException {
        ArrayList<ToolManager.ToolMode> tools = new ArrayList<>();
        for (int i = 0; i < args.length(); i++) {
            String item = args.getString(i);
            ToolManager.ToolMode mode = convStringToToolMode(item);
            if (mode != null) {
                tools.add(mode);
            }
        }
        return tools;
    }

    private static ToolManager.ToolMode convStringToToolMode(String item) {
        ToolManager.ToolMode mode = null;
        if (TOOL_BUTTON_FREE_HAND.equals(item) || TOOL_ANNOTATION_CREATE_FREE_HAND.equals(item)) {
            mode = ToolManager.ToolMode.INK_CREATE;
        } else if (TOOL_BUTTON_HIGHLIGHT.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_HIGHLIGHT.equals(item)) {
            mode = ToolManager.ToolMode.TEXT_HIGHLIGHT;
        } else if (TOOL_BUTTON_UNDERLINE.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_UNDERLINE.equals(item)) {
            mode = ToolManager.ToolMode.TEXT_UNDERLINE;
        } else if (TOOL_BUTTON_SQUIGGLY.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_SQUIGGLY.equals(item)) {
            mode = ToolManager.ToolMode.TEXT_SQUIGGLY;
        } else if (TOOL_BUTTON_STRIKEOUT.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_STRIKEOUT.equals(item)) {
            mode = ToolManager.ToolMode.TEXT_STRIKEOUT;
        } else if (TOOL_BUTTON_RECTANGLE.equals(item) || TOOL_ANNOTATION_CREATE_RECTANGLE.equals(item)) {
            mode = ToolManager.ToolMode.RECT_CREATE;
        } else if (TOOL_BUTTON_ELLIPSE.equals(item) || TOOL_ANNOTATION_CREATE_ELLIPSE.equals(item)) {
            mode = ToolManager.ToolMode.OVAL_CREATE;
        } else if (TOOL_BUTTON_LINE.equals(item) || TOOL_ANNOTATION_CREATE_LINE.equals(item)) {
            mode = ToolManager.ToolMode.LINE_CREATE;
        } else if (TOOL_BUTTON_ARROW.equals(item) || TOOL_ANNOTATION_CREATE_ARROW.equals(item)) {
            mode = ToolManager.ToolMode.ARROW_CREATE;
        } else if (TOOL_BUTTON_POLYLINE.equals(item) || TOOL_ANNOTATION_CREATE_POLYLINE.equals(item)) {
            mode = ToolManager.ToolMode.POLYLINE_CREATE;
        } else if (TOOL_BUTTON_POLYGON.equals(item) || TOOL_ANNOTATION_CREATE_POLYGON.equals(item)) {
            mode = ToolManager.ToolMode.POLYGON_CREATE;
        } else if (TOOL_BUTTON_CLOUD.equals(item) || TOOL_ANNOTATION_CREATE_POLYGON_CLOUD.equals(item)) {
            mode = ToolManager.ToolMode.CLOUD_CREATE;
        } else if (TOOL_BUTTON_SIGNATURE.equals(item) || TOOL_ANNOTATION_CREATE_SIGNATURE.equals(item)) {
            mode = ToolManager.ToolMode.SIGNATURE;
        } else if (TOOL_BUTTON_FREE_TEXT.equals(item) || TOOL_ANNOTATION_CREATE_FREE_TEXT.equals(item)) {
            mode = ToolManager.ToolMode.TEXT_CREATE;
        } else if (TOOL_BUTTON_STICKY.equals(item) || TOOL_ANNOTATION_CREATE_STICKY.equals(item)) {
            mode = ToolManager.ToolMode.TEXT_ANNOT_CREATE;
        } else if (TOOL_BUTTON_CALLOUT.equals(item) || TOOL_ANNOTATION_CREATE_CALLOUT.equals(item)) {
            mode = ToolManager.ToolMode.CALLOUT_CREATE;
        } else if (TOOL_BUTTON_STAMP.equals(item) || TOOL_ANNOTATION_CREATE_STAMP.equals(item)) {
            mode = ToolManager.ToolMode.STAMPER;
        } else if (TOOL_ANNOTATION_CREATE_DISTANCE_MEASUREMENT.equals(item)) {
            mode = ToolManager.ToolMode.RULER_CREATE;
        } else if (TOOL_ANNOTATION_CREATE_PERIMETER_MEASUREMENT.equals(item)) {
            mode = ToolManager.ToolMode.PERIMETER_MEASURE_CREATE;
        } else if (TOOL_ANNOTATION_CREATE_RECTANGLE_AREA_MEASUREMENT.equals(item)) {
            mode = ToolManager.ToolMode.RECT_AREA_MEASURE_CREATE;
        } else if (TOOL_ANNOTATION_CREATE_AREA_MEASUREMENT.equals(item)) {
            mode = ToolManager.ToolMode.AREA_MEASURE_CREATE;
        } else if (TOOL_TEXT_SELECT.equals(item)) {
            mode = ToolManager.ToolMode.TEXT_SELECT;
        } else if (TOOL_ANNOTATION_EDIT.equals(item)) {
            mode = ToolManager.ToolMode.ANNOT_EDIT_RECT_GROUP;
        } else if (TOOL_ANNOTATION_CREATE_SOUND.equals(item)) {
            mode = ToolManager.ToolMode.SOUND_CREATE;
        } else if (TOOL_ANNOTATION_CREATE_FREE_HIGHLIGHTER.equals(item)) {
            mode = ToolManager.ToolMode.FREE_HIGHLIGHTER;
        } else if (TOOL_ANNOTATION_CREATE_RUBBER_STAMP.equals(item)) {
            mode = ToolManager.ToolMode.RUBBER_STAMPER;
        } else if (TOOL_ERASER.equals(item)) {
            mode = ToolManager.ToolMode.INK_ERASER;
        } else if (TOOL_ANNOTATION_CREATE_FILE_ATTACHMENT.equals(item)) {
            mode = ToolManager.ToolMode.FILE_ATTACHMENT_CREATE;
        } else if (TOOL_ANNOTATION_CREATE_REDACTION.equals(item)) {
            mode = ToolManager.ToolMode.RECT_REDACTION;
        } else if (TOOL_ANNOTATION_CREATE_LINK.equals(item)) {
            mode = ToolManager.ToolMode.RECT_LINK;
        } else if (TOOL_ANNOTATION_CREATE_REDACTION_TEXT.equals(item)) {
            mode = ToolManager.ToolMode.TEXT_REDACTION;
        } else if (TOOL_ANNOTATION_CREATE_LINK_TEXT.equals(item)) {
            mode = ToolManager.ToolMode.TEXT_LINK_CREATE;
        } else if (TOOL_FORM_CREATE_TEXT_FIELD.equals(item)) {
            mode = ToolManager.ToolMode.FORM_TEXT_FIELD_CREATE;
        } else if (TOOL_FORM_CREATE_CHECKBOX_FIELD.equals(item)) {
            mode = ToolManager.ToolMode.FORM_CHECKBOX_CREATE;
        } else if (TOOL_FORM_CREATE_SIGNATURE_FIELD.equals(item)) {
            mode = ToolManager.ToolMode.FORM_SIGNATURE_CREATE;
        } else if (TOOL_FORM_CREATE_RADIO_FIELD.equals(item)) {
            mode = ToolManager.ToolMode.FORM_RADIO_GROUP_CREATE;
        } else if (TOOL_FORM_CREATE_COMBO_BOX_FIELD.equals(item)) {
            mode = ToolManager.ToolMode.FORM_COMBO_BOX_CREATE;
        } else if (TOOL_FORM_CREATE_LIST_BOX_FIELD.equals(item)) {
            mode = ToolManager.ToolMode.FORM_LIST_BOX_CREATE;
        } else if (TOOL_ANNOTATION_SMART_PEN.equals(item)) {
            mode = ToolManager.ToolMode.SMART_PEN_INK;
        } else if (TOOL_ANNOTATION_LASSO.equals(item)) {
            mode = ToolManager.ToolMode.ANNOT_EDIT_RECT_GROUP;
        }
        return mode;
    }

    private static String convStringToLayoutMode(String layoutString) {
        String layoutMode = null;
        if (LAYOUT_MODE_SINGLE.equals(layoutString)) {
            layoutMode = PdfViewCtrlSettingsManager.KEY_PREF_VIEWMODE_SINGLEPAGE_VALUE;
        } else if (LAYOUT_MODE_CONTINUOUS.equals(layoutString)) {
            layoutMode = PdfViewCtrlSettingsManager.KEY_PREF_VIEWMODE_CONTINUOUS_VALUE;
        } else if (LAYOUT_MODE_FACING.equals(layoutString)) {
            layoutMode = PdfViewCtrlSettingsManager.KEY_PREF_VIEWMODE_FACING_VALUE;
        } else if (LAYOUT_MODE_FACING_CONTINUOUS.equals(layoutString)) {
            layoutMode = PdfViewCtrlSettingsManager.KEY_PREF_VIEWMODE_FACING_CONT_VALUE;
        } else if (LAYOUT_MODE_FACING_OVER.equals(layoutString)) {
            layoutMode = PdfViewCtrlSettingsManager.KEY_PREF_VIEWMODE_FACINGCOVER_VALUE;
        } else if (LAYOUT_MODE_FACING_OVER_CONTINUOUS.equals(layoutString)) {
            layoutMode = PdfViewCtrlSettingsManager.KEY_PREF_VIEWMODE_FACINGCOVER_CONT_VALUE;
        }
        return layoutMode;
    }

    private static PDFViewCtrl.PagePresentationMode convStringToPagePresentationMode(String pagePresentationString) {
        PDFViewCtrl.PagePresentationMode mode = null;
        if (LAYOUT_MODE_SINGLE.equals(pagePresentationString)) {
            mode = PDFViewCtrl.PagePresentationMode.SINGLE;
        } else if (LAYOUT_MODE_CONTINUOUS.equals(pagePresentationString)) {
            mode = PDFViewCtrl.PagePresentationMode.SINGLE_CONT;
        } else if (LAYOUT_MODE_FACING.equals(pagePresentationString)) {
            mode = PDFViewCtrl.PagePresentationMode.FACING;
        } else if (LAYOUT_MODE_FACING_CONTINUOUS.equals(pagePresentationString)) {
            mode = PDFViewCtrl.PagePresentationMode.FACING_CONT;
        } else if (LAYOUT_MODE_FACING_OVER.equals(pagePresentationString)) {
            mode = PDFViewCtrl.PagePresentationMode.FACING_COVER;
        } else if (LAYOUT_MODE_FACING_OVER_CONTINUOUS.equals(pagePresentationString)) {
            mode = PDFViewCtrl.PagePresentationMode.FACING_COVER_CONT;
        }
        return mode;
    }

    private static PDFViewCtrl.PageViewMode convStringToFitMode(String fitString) {
        PDFViewCtrl.PageViewMode fitMode = null;
        if (FIT_MODE_FIT_PAGE.equals(fitString)) {
            fitMode = PDFViewCtrl.PageViewMode.FIT_PAGE;
        } else if (FIT_MODE_FIT_WIDTH.equals(fitString)) {
            fitMode = PDFViewCtrl.PageViewMode.FIT_WIDTH;
        } else if (FIT_MODE_FIT_HEIGHT.equals(fitString)) {
            fitMode = PDFViewCtrl.PageViewMode.FIT_HEIGHT;
        } else if (FIT_MODE_ZOOM.equals(fitString)) {
            fitMode = PDFViewCtrl.PageViewMode.ZOOM;
        }
        return fitMode;
    }

    @Nullable
    public static int convStringToAnnotType(String item) {
        int annotType = Annot.e_Unknown;
        if (TOOL_BUTTON_FREE_HAND.equals(item) || TOOL_ANNOTATION_CREATE_FREE_HAND.equals(item)) {
            annotType = Annot.e_Ink;
        } else if (TOOL_BUTTON_HIGHLIGHT.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_HIGHLIGHT.equals(item)) {
            annotType = Annot.e_Highlight;
        } else if (TOOL_BUTTON_UNDERLINE.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_UNDERLINE.equals(item)) {
            annotType = Annot.e_Underline;
        } else if (TOOL_BUTTON_SQUIGGLY.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_SQUIGGLY.equals(item)) {
            annotType = Annot.e_Squiggly;
        } else if (TOOL_BUTTON_STRIKEOUT.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_STRIKEOUT.equals(item)) {
            annotType = Annot.e_StrikeOut;
        } else if (TOOL_BUTTON_RECTANGLE.equals(item) || TOOL_ANNOTATION_CREATE_RECTANGLE.equals(item)) {
            annotType = Annot.e_Square;
        } else if (TOOL_BUTTON_ELLIPSE.equals(item) || TOOL_ANNOTATION_CREATE_ELLIPSE.equals(item)) {
            annotType = Annot.e_Circle;
        } else if (TOOL_BUTTON_LINE.equals(item) || TOOL_ANNOTATION_CREATE_LINE.equals(item)) {
            annotType = Annot.e_Line;
        } else if (TOOL_BUTTON_ARROW.equals(item) || TOOL_ANNOTATION_CREATE_ARROW.equals(item)) {
            annotType = AnnotStyle.CUSTOM_ANNOT_TYPE_ARROW;
        } else if (TOOL_BUTTON_POLYLINE.equals(item) || TOOL_ANNOTATION_CREATE_POLYLINE.equals(item)) {
            annotType = Annot.e_Polyline;
        } else if (TOOL_BUTTON_POLYGON.equals(item) || TOOL_ANNOTATION_CREATE_POLYGON.equals(item)) {
            annotType = Annot.e_Polygon;
        } else if (TOOL_BUTTON_CLOUD.equals(item) || TOOL_ANNOTATION_CREATE_POLYGON_CLOUD.equals(item)) {
            annotType = AnnotStyle.CUSTOM_ANNOT_TYPE_CLOUD;
        } else if (TOOL_BUTTON_SIGNATURE.equals(item) || TOOL_ANNOTATION_CREATE_SIGNATURE.equals(item)) {
            annotType = AnnotStyle.CUSTOM_ANNOT_TYPE_SIGNATURE;
        } else if (TOOL_BUTTON_FREE_TEXT.equals(item) || TOOL_ANNOTATION_CREATE_FREE_TEXT.equals(item)) {
            annotType = Annot.e_FreeText;
        } else if (TOOL_BUTTON_STICKY.equals(item) || TOOL_ANNOTATION_CREATE_STICKY.equals(item)) {
            annotType = Annot.e_Text;
        } else if (TOOL_BUTTON_CALLOUT.equals(item) || TOOL_ANNOTATION_CREATE_CALLOUT.equals(item)) {
            annotType = AnnotStyle.CUSTOM_ANNOT_TYPE_CALLOUT;
        } else if (TOOL_BUTTON_STAMP.equals(item) || TOOL_ANNOTATION_CREATE_STAMP.equals(item)) {
            annotType = Annot.e_Stamp;
        } else if (TOOL_ANNOTATION_CREATE_DISTANCE_MEASUREMENT.equals(item)) {
            annotType = AnnotStyle.CUSTOM_ANNOT_TYPE_RULER;
        } else if (TOOL_ANNOTATION_CREATE_PERIMETER_MEASUREMENT.equals(item)) {
            annotType = AnnotStyle.CUSTOM_ANNOT_TYPE_PERIMETER_MEASURE;
        } else if (TOOL_ANNOTATION_CREATE_AREA_MEASUREMENT.equals(item)) {
            annotType = AnnotStyle.CUSTOM_ANNOT_TYPE_AREA_MEASURE;
        } else if (TOOL_ANNOTATION_CREATE_FILE_ATTACHMENT.equals(item)) {
            annotType = Annot.e_FileAttachment;
        } else if (TOOL_ANNOTATION_CREATE_SOUND.equals(item)) {
            annotType = Annot.e_Sound;
        } else if (TOOL_ANNOTATION_CREATE_REDACTION.equals(item) || TOOL_ANNOTATION_CREATE_REDACTION_TEXT.equals(item)) {
            annotType = Annot.e_Redact;
        } else if (TOOL_ANNOTATION_CREATE_LINK.equals(item) || TOOL_ANNOTATION_CREATE_LINK_TEXT.equals(item)) {
            annotType = Annot.e_Link;
        } else if (TOOL_FORM_CREATE_TEXT_FIELD.equals(item)) {
            annotType = Annot.e_Widget;
        } else if (TOOL_FORM_CREATE_CHECKBOX_FIELD.equals(item)) {
            annotType = Annot.e_Widget;
        } else if (TOOL_FORM_CREATE_SIGNATURE_FIELD.equals(item)) {
            annotType = Annot.e_Widget;
        } else if (TOOL_FORM_CREATE_RADIO_FIELD.equals(item)) {
            annotType = Annot.e_Widget;
        } else if (TOOL_FORM_CREATE_COMBO_BOX_FIELD.equals(item)) {
            annotType = Annot.e_Widget;
        } else if (TOOL_FORM_CREATE_TOOL_BOX_FIELD.equals(item)) {
            annotType = Annot.e_Widget;
        }
        return annotType;
    }

    @Nullable
    public static String convQuickMenuIdToString(int id) {
        String menuStr = null;
        if (id == R.id.qm_appearance) {
            menuStr = MENU_ID_STRING_STYLE;
        } else if (id == R.id.qm_note) {
            menuStr = MENU_ID_STRING_NOTE;
        } else if (id == R.id.qm_copy) {
            menuStr = MENU_ID_STRING_COPY;
        } else if (id == R.id.qm_delete) {
            menuStr = MENU_ID_STRING_DELETE;
        } else if (id == R.id.qm_flatten) {
            menuStr = MENU_ID_STRING_FLATTEN;
        } else if (id == R.id.qm_text) {
            menuStr = MENU_ID_STRING_TEXT;
        } else if (id == R.id.qm_edit) {
            menuStr = MENU_ID_STRING_EDIT_INK;
        } else if (id == R.id.qm_search) {
            menuStr = MENU_ID_STRING_SEARCH;
        } else if (id == R.id.qm_share) {
            menuStr = MENU_ID_STRING_SHARE;
        } else if (id == R.id.qm_type) {
            menuStr = MENU_ID_STRING_MARKUP_TYPE;
        } else if (id == R.id.qm_screencap_create) {
            menuStr = MENU_ID_STRING_SCREEN_CAPTURE;
        } else if (id == R.id.qm_play_sound) {
            menuStr = MENU_ID_STRING_PLAY_SOUND;
        } else if (id == R.id.qm_open_attachment) {
            menuStr = MENU_ID_STRING_OPEN_ATTACHMENT;
        } else if (id == R.id.qm_tts) {
            menuStr = MENU_ID_STRING_READ;
        } else if (id == R.id.qm_calibrate) {
            menuStr = MENU_ID_STRING_CALIBRATE;
        } else if (id == R.id.qm_underline) {
            menuStr = MENU_ID_STRING_UNDERLINE;
        } else if (id == R.id.qm_redact) {
            menuStr = MENU_ID_STRING_REDACT;
        } else if (id == R.id.qm_redaction) {
            menuStr = MENU_ID_STRING_REDACTION;
        } else if (id == R.id.qm_strikeout) {
            menuStr = MENU_ID_STRING_STRIKEOUT;
        } else if (id == R.id.qm_squiggly) {
            menuStr = MENU_ID_STRING_SQUIGGLY;
        } else if (id == R.id.qm_link) {
            menuStr = MENU_ID_STRING_LINK;
        } else if (id == R.id.qm_highlight) {
            menuStr = MENU_ID_STRING_HIGHLIGHT;
        } else if (id == R.id.qm_floating_sig) {
            menuStr = MENU_ID_STRING_SIGNATURE;
        } else if (id == R.id.qm_rectangle) {
            menuStr = MENU_ID_STRING_RECTANGLE;
        } else if (id == R.id.qm_line) {
            menuStr = MENU_ID_STRING_LINE;
        } else if (id == R.id.qm_free_hand) {
            menuStr = MENU_ID_STRING_FREE_HAND;
        } else if (id == R.id.qm_image_stamper) {
            menuStr = MENU_ID_STRING_IMAGE;
        } else if (id == R.id.qm_form_text) {
            menuStr = MENU_ID_STRING_FORM_TEXT;
        } else if (id == R.id.qm_sticky_note) {
            menuStr = MENU_ID_STRING_STICKY_NOTE;
        } else if (id == R.id.qm_overflow) {
            menuStr = MENU_ID_STRING_OVERFLOW;
        } else if (id == R.id.qm_ink_eraser) {
            menuStr = MENU_ID_STRING_ERASER;
        } else if (id == R.id.qm_rubber_stamper) {
            menuStr = MENU_ID_STRING_STAMP;
        } else if (id == R.id.qm_page_redaction) {
            menuStr = MENU_ID_STRING_PAGE_REDACTION;
        } else if (id == R.id.qm_rect_redaction) {
            menuStr = MENU_ID_STRING_RECT_REDACTION;
        } else if (id == R.id.qm_search_redaction) {
            menuStr = MENU_ID_STRING_SEARCH_REDACTION;
        } else if (id == R.id.qm_shape) {
            menuStr = MENU_ID_STRING_SHAPE;
        } else if (id == R.id.qm_cloud) {
            menuStr = MENU_ID_STRING_CLOUD;
        } else if (id == R.id.qm_polygon) {
            menuStr = MENU_ID_STRING_POLYGON;
        } else if (id == R.id.qm_polyline) {
            menuStr = MENU_ID_STRING_POLYLINE;
        } else if (id == R.id.qm_free_highlighter) {
            menuStr = MENU_ID_STRING_FREE_HIGHLIGHTER;
        } else if (id == R.id.qm_arrow) {
            menuStr = MENU_ID_STRING_ARROW;
        } else if (id == R.id.qm_oval) {
            menuStr = MENU_ID_STRING_OVAL;
        } else if (id == R.id.qm_callout) {
            menuStr = MENU_ID_STRING_CALLOUT;
        } else if (id == R.id.qm_measurement) {
            menuStr = MENU_ID_STRING_MEASUREMENT;
        } else if (id == R.id.qm_area_measure) {
            menuStr = MENU_ID_STRING_AREA_MEASUREMENT;
        } else if (id == R.id.qm_perimeter_measure) {
            menuStr = MENU_ID_STRING_PERIMETER_MEASUREMENT;
        } else if (id == R.id.qm_rect_area_measure) {
            menuStr = MENU_ID_STRING_RECT_AREA_MEASUREMENT;
        } else if (id == R.id.qm_ruler) {
            menuStr = MENU_ID_STRING_RULER;
        } else if (id == R.id.qm_form) {
            menuStr = MENU_ID_STRING_FORM;
        } else if (id == R.id.qm_form_combo_box) {
            menuStr = MENU_ID_STRING_FORM_COMBO_BOX;
        } else if (id == R.id.qm_form_list_box) {
            menuStr = MENU_ID_STRING_FORM_LIST_BOX;
        } else if (id == R.id.qm_form_check_box) {
            menuStr = MENU_ID_STRING_FORM_CHECK_BOX;
        } else if (id == R.id.qm_form_signature) {
            menuStr = MENU_ID_STRING_FORM_SIGNATURE;
        } else if (id == R.id.qm_form_radio_group) {
            menuStr = MENU_ID_STRING_FORM_RADIO_GROUP;
        } else if (id == R.id.qm_attach) {
            menuStr = MENU_ID_STRING_ATTACH;
        } else if (id == R.id.qm_file_attachment) {
            menuStr = MENU_ID_STRING_FILE_ATTACHMENT;
        } else if (id == R.id.qm_sound) {
            menuStr = MENU_ID_STRING_SOUND;
        } else if (id == R.id.qm_free_text) {
            menuStr = MENU_ID_STRING_FREE_TEXT;
        } else if (id == R.id.qm_crop) {
            menuStr = MENU_ID_STRING_CROP;
        } else if (id == R.id.qm_crop_ok) {
            menuStr = MENU_ID_STRING_CROP_OK;
        } else if (id == R.id.qm_crop_cancel) {
            menuStr = MENU_ID_STRING_CROP_CANCEL;
        } else if (id == R.id.qm_define) {
            menuStr = MENU_ID_STRING_DEFINE;
        } else if (id == R.id.qm_field_signed) {
            menuStr = MENU_ID_STRING_FIELD_SIGNED;
        } else if (id == R.id.qm_first_row_group) {
            menuStr = MENU_ID_STRING_FIRST_ROW_GROUP;
        } else if (id == R.id.qm_second_row_group) {
            menuStr = MENU_ID_STRING_SECOND_ROW_GROUP;
        } else if (id == R.id.qm_group) {
            menuStr = MENU_ID_STRING_GROUP;
        } else if (id == R.id.qm_paste) {
            menuStr = MENU_ID_STRING_PASTE;
        } else if (id == R.id.qm_rect_group_select) {
            menuStr = MENU_ID_STRING_RECT_GROUP_SELECT;
        } else if (id == R.id.qm_sign_and_save) {
            menuStr = MENU_ID_STRING_SIGN_AND_SAVE;
        } else if (id == R.id.qm_thickness) {
            menuStr = MENU_ID_STRING_THICKNESS;
        } else if (id == R.id.qm_translate) {
            menuStr = MENU_ID_STRING_TRANSLATE;
        } else if (id == R.id.qm_ungroup) {
            menuStr = MENU_ID_STRING_UNGROUP;
        }

        return menuStr;
    }

    private static boolean isValidToolbarTag(String tag) {
        if (tag != null) {
            if (TAG_VIEW_TOOLBAR.equals(tag) ||
                    TAG_ANNOTATE_TOOLBAR.equals(tag) ||
                    TAG_DRAW_TOOLBAR.equals(tag) ||
                    TAG_INSERT_TOOLBAR.equals(tag) ||
                    TAG_FILL_AND_SIGN_TOOLBAR.equals(tag) ||
                    TAG_PREPARE_FORM_TOOLBAR.equals(tag) ||
                    TAG_MEASURE_TOOLBAR.equals(tag) ||
                    TAG_PENS_TOOLBAR.equals(tag) ||
                    TAG_REDACTION_TOOLBAR.equals(tag) ||
                    TAG_FAVORITE_TOOLBAR.equals(tag)) {
                return true;
            }
        }
        return false;
    }

    private static int convStringToButtonId(String item) {
        int buttonId = 0;
        if (TOOL_BUTTON_FREE_HAND.equals(item) || TOOL_ANNOTATION_CREATE_FREE_HAND.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.INK.value();
        } else if (TOOL_BUTTON_HIGHLIGHT.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_HIGHLIGHT.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.TEXT_HIGHLIGHT.value();
        } else if (TOOL_BUTTON_UNDERLINE.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_UNDERLINE.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.TEXT_UNDERLINE.value();
        } else if (TOOL_BUTTON_SQUIGGLY.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_SQUIGGLY.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.TEXT_SQUIGGLY.value();
        } else if (TOOL_BUTTON_STRIKEOUT.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_STRIKEOUT.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.TEXT_STRIKEOUT.value();
        } else if (TOOL_BUTTON_RECTANGLE.equals(item) || TOOL_ANNOTATION_CREATE_RECTANGLE.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.SQUARE.value();
        } else if (TOOL_BUTTON_ELLIPSE.equals(item) || TOOL_ANNOTATION_CREATE_ELLIPSE.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.CIRCLE.value();
        } else if (TOOL_BUTTON_LINE.equals(item) || TOOL_ANNOTATION_CREATE_LINE.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.LINE.value();
        } else if (TOOL_BUTTON_ARROW.equals(item) || TOOL_ANNOTATION_CREATE_ARROW.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.ARROW.value();
        } else if (TOOL_BUTTON_POLYLINE.equals(item) || TOOL_ANNOTATION_CREATE_POLYLINE.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.POLYLINE.value();
        } else if (TOOL_BUTTON_POLYGON.equals(item) || TOOL_ANNOTATION_CREATE_POLYGON.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.POLYGON.value();
        } else if (TOOL_BUTTON_CLOUD.equals(item) || TOOL_ANNOTATION_CREATE_POLYGON_CLOUD.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.POLY_CLOUD.value();
        } else if (TOOL_BUTTON_SIGNATURE.equals(item) || TOOL_ANNOTATION_CREATE_SIGNATURE.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.SIGNATURE.value();
        } else if (TOOL_BUTTON_FREE_TEXT.equals(item) || TOOL_ANNOTATION_CREATE_FREE_TEXT.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.FREE_TEXT.value();
        } else if (TOOL_BUTTON_STICKY.equals(item) || TOOL_ANNOTATION_CREATE_STICKY.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.STICKY_NOTE.value();
        } else if (TOOL_BUTTON_CALLOUT.equals(item) || TOOL_ANNOTATION_CREATE_CALLOUT.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.CALLOUT.value();
        } else if (TOOL_BUTTON_STAMP.equals(item) || TOOL_ANNOTATION_CREATE_STAMP.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.STAMP.value();
        } else if (TOOL_ANNOTATION_CREATE_RUBBER_STAMP.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.STAMP.value();
        } else if (TOOL_ANNOTATION_CREATE_DISTANCE_MEASUREMENT.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.RULER.value();
        } else if (TOOL_ANNOTATION_CREATE_PERIMETER_MEASUREMENT.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.PERIMETER.value();
        } else if (TOOL_ANNOTATION_CREATE_AREA_MEASUREMENT.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.AREA.value();
        } else if (TOOL_ANNOTATION_CREATE_FILE_ATTACHMENT.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.ATTACHMENT.value();
        } else if (TOOL_ANNOTATION_CREATE_SOUND.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.SOUND.value();
        } else if (TOOL_ANNOTATION_CREATE_REDACTION.equals(item)) {
            // TODO
        } else if (TOOL_ANNOTATION_CREATE_LINK.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.LINK.value();
        } else if (TOOL_ANNOTATION_CREATE_REDACTION_TEXT.equals(item)) {
            // TODO
        } else if (TOOL_ANNOTATION_CREATE_LINK_TEXT.equals(item)) {
            // TODO
        } else if (TOOL_ANNOTATION_EDIT.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.MULTI_SELECT.value();
        } else if (TOOL_FORM_CREATE_TEXT_FIELD.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.TEXT_FIELD.value();
        } else if (TOOL_FORM_CREATE_CHECKBOX_FIELD.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.CHECKBOX.value();
        } else if (TOOL_FORM_CREATE_SIGNATURE_FIELD.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.SIGNATURE_FIELD.value();
        } else if (TOOL_FORM_CREATE_RADIO_FIELD.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.RADIO_BUTTON.value();
        } else if (TOOL_FORM_CREATE_COMBO_BOX_FIELD.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.COMBO_BOX.value();
        } else if (TOOL_FORM_CREATE_LIST_BOX_FIELD.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.LIST_BOX.value();
        } else if (TOOL_ERASER.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.ERASER.value();
        } else if (BUTTON_UNDO.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.UNDO.value();
        } else if (BUTTON_REDO.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.REDO.value();
        } else if (TOOL_ANNOTATION_CREATE_FREE_HIGHLIGHTER.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.FREE_HIGHLIGHT.value();
        } else if (TOOL_ANNOTATION_SMART_PEN.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.SMART_PEN.value();
        } else if (BUTTON_EDIT_ANNOTATION_TOOLBAR.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.CUSTOMIZE.value();
        } else if (TOOL_ANNOTATION_LASSO.equals(item)) {
            buttonId = DefaultToolbars.ButtonId.LASSO_SELECT.value();
        }
        return buttonId;
    }

    @Nullable
    private static ToolbarButtonType convStringToToolbarType(String item) {
        ToolbarButtonType buttonType = null;
        if (TOOL_BUTTON_FREE_HAND.equals(item) || TOOL_ANNOTATION_CREATE_FREE_HAND.equals(item)) {
            buttonType = ToolbarButtonType.INK;
        } else if (TOOL_BUTTON_HIGHLIGHT.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_HIGHLIGHT.equals(item)) {
            buttonType = ToolbarButtonType.TEXT_HIGHLIGHT;
        } else if (TOOL_BUTTON_UNDERLINE.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_UNDERLINE.equals(item)) {
            buttonType = ToolbarButtonType.TEXT_UNDERLINE;
        } else if (TOOL_BUTTON_SQUIGGLY.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_SQUIGGLY.equals(item)) {
            buttonType = ToolbarButtonType.TEXT_SQUIGGLY;
        } else if (TOOL_BUTTON_STRIKEOUT.equals(item) || TOOL_ANNOTATION_CREATE_TEXT_STRIKEOUT.equals(item)) {
            buttonType = ToolbarButtonType.TEXT_STRIKEOUT;
        } else if (TOOL_BUTTON_RECTANGLE.equals(item) || TOOL_ANNOTATION_CREATE_RECTANGLE.equals(item)) {
            buttonType = ToolbarButtonType.SQUARE;
        } else if (TOOL_BUTTON_ELLIPSE.equals(item) || TOOL_ANNOTATION_CREATE_ELLIPSE.equals(item)) {
            buttonType = ToolbarButtonType.CIRCLE;
        } else if (TOOL_BUTTON_LINE.equals(item) || TOOL_ANNOTATION_CREATE_LINE.equals(item)) {
            buttonType = ToolbarButtonType.LINE;
        } else if (TOOL_BUTTON_ARROW.equals(item) || TOOL_ANNOTATION_CREATE_ARROW.equals(item)) {
            buttonType = ToolbarButtonType.ARROW;
        } else if (TOOL_BUTTON_POLYLINE.equals(item) || TOOL_ANNOTATION_CREATE_POLYLINE.equals(item)) {
            buttonType = ToolbarButtonType.POLYLINE;
        } else if (TOOL_BUTTON_POLYGON.equals(item) || TOOL_ANNOTATION_CREATE_POLYGON.equals(item)) {
            buttonType = ToolbarButtonType.POLYGON;
        } else if (TOOL_BUTTON_CLOUD.equals(item) || TOOL_ANNOTATION_CREATE_POLYGON_CLOUD.equals(item)) {
            buttonType = ToolbarButtonType.POLY_CLOUD;
        } else if (TOOL_BUTTON_SIGNATURE.equals(item) || TOOL_ANNOTATION_CREATE_SIGNATURE.equals(item)) {
            buttonType = ToolbarButtonType.SIGNATURE;
        } else if (TOOL_BUTTON_FREE_TEXT.equals(item) || TOOL_ANNOTATION_CREATE_FREE_TEXT.equals(item)) {
            buttonType = ToolbarButtonType.FREE_TEXT;
        } else if (TOOL_BUTTON_STICKY.equals(item) || TOOL_ANNOTATION_CREATE_STICKY.equals(item)) {
            buttonType = ToolbarButtonType.STICKY_NOTE;
        } else if (TOOL_BUTTON_CALLOUT.equals(item) || TOOL_ANNOTATION_CREATE_CALLOUT.equals(item)) {
            buttonType = ToolbarButtonType.CALLOUT;
        } else if (TOOL_BUTTON_STAMP.equals(item) || TOOL_ANNOTATION_CREATE_STAMP.equals(item)) {
            buttonType = ToolbarButtonType.STAMP;
        } else if (TOOL_ANNOTATION_CREATE_RUBBER_STAMP.equals(item)) {
            buttonType = ToolbarButtonType.STAMP;
        } else if (TOOL_ANNOTATION_CREATE_DISTANCE_MEASUREMENT.equals(item)) {
            buttonType = ToolbarButtonType.RULER;
        } else if (TOOL_ANNOTATION_CREATE_PERIMETER_MEASUREMENT.equals(item)) {
            buttonType = ToolbarButtonType.PERIMETER;
        } else if (TOOL_ANNOTATION_CREATE_AREA_MEASUREMENT.equals(item)) {
            buttonType = ToolbarButtonType.AREA;
        } else if (TOOL_ANNOTATION_CREATE_FILE_ATTACHMENT.equals(item)) {
            buttonType = ToolbarButtonType.ATTACHMENT;
        } else if (TOOL_ANNOTATION_CREATE_SOUND.equals(item)) {
            buttonType = ToolbarButtonType.SOUND;
        } else if (TOOL_ANNOTATION_CREATE_REDACTION.equals(item)) {
            // TODO
        } else if (TOOL_ANNOTATION_CREATE_LINK.equals(item)) {
            buttonType = ToolbarButtonType.LINK;
        } else if (TOOL_ANNOTATION_CREATE_REDACTION_TEXT.equals(item)) {
            // TODO
        } else if (TOOL_ANNOTATION_CREATE_LINK_TEXT.equals(item)) {
            // TODO
        } else if (TOOL_ANNOTATION_EDIT.equals(item)) {
            buttonType = ToolbarButtonType.MULTI_SELECT;
        } else if (TOOL_FORM_CREATE_TEXT_FIELD.equals(item)) {
            buttonType = ToolbarButtonType.TEXT_FIELD;
        } else if (TOOL_FORM_CREATE_CHECKBOX_FIELD.equals(item)) {
            buttonType = ToolbarButtonType.CHECKBOX;
        } else if (TOOL_FORM_CREATE_SIGNATURE_FIELD.equals(item)) {
            buttonType = ToolbarButtonType.SIGNATURE_FIELD;
        } else if (TOOL_FORM_CREATE_RADIO_FIELD.equals(item)) {
            buttonType = ToolbarButtonType.RADIO_BUTTON;
        } else if (TOOL_FORM_CREATE_COMBO_BOX_FIELD.equals(item)) {
            buttonType = ToolbarButtonType.COMBO_BOX;
        } else if (TOOL_FORM_CREATE_LIST_BOX_FIELD.equals(item)) {
            buttonType = ToolbarButtonType.LIST_BOX;
        } else if (TOOL_ERASER.equals(item)) {
            buttonType = ToolbarButtonType.ERASER;
        } else if (BUTTON_UNDO.equals(item)) {
            buttonType = ToolbarButtonType.UNDO;
        } else if (BUTTON_REDO.equals(item)) {
            buttonType = ToolbarButtonType.REDO;
        } else if (TOOL_ANNOTATION_CREATE_FREE_HIGHLIGHTER.equals(item)) {
            buttonType = ToolbarButtonType.FREE_HIGHLIGHT;
        } else if (TOOL_ANNOTATION_SMART_PEN.equals(item)) {
            buttonType = ToolbarButtonType.SMART_PEN;
        } else if (BUTTON_EDIT_ANNOTATION_TOOLBAR.equals(item)) {
            buttonType = ToolbarButtonType.EDIT_TOOLBAR;
        } else if (TOOL_ANNOTATION_LASSO.equals(item)) {
            buttonType = ToolbarButtonType.LASSO_SELECT;
        }
        return buttonType;
    }

    private static int convStringToToolbarDefaultIconRes(String item) {
        if (TAG_VIEW_TOOLBAR.equals(item)) {
            return R.drawable.ic_view;
        } else if (TAG_ANNOTATE_TOOLBAR.equals(item)) {
            return R.drawable.ic_annotation_underline_black_24dp;
        } else if (TAG_DRAW_TOOLBAR.equals(item)) {
            return R.drawable.ic_pens_and_shapes;
        } else if (TAG_INSERT_TOOLBAR.equals(item)) {
            return R.drawable.ic_add_image_white;
        } else if (TAG_FILL_AND_SIGN_TOOLBAR.equals(item)) {
            return R.drawable.ic_fill_and_sign;
        } else if (TAG_PREPARE_FORM_TOOLBAR.equals(item)) {
            return R.drawable.ic_prepare_form;
        } else if (TAG_MEASURE_TOOLBAR.equals(item)) {
            return R.drawable.ic_annotation_distance_black_24dp;
        } else if (TAG_PENS_TOOLBAR.equals(item)) {
            return R.drawable.ic_annotation_freehand_black_24dp;
        } else if (TAG_REDACTION_TOOLBAR.equals(item)) {
            return R.drawable.ic_annotation_redact_black_24dp;
        } else if (TAG_FAVORITE_TOOLBAR.equals(item)) {
            return R.drawable.ic_star_white_24dp;
        }
        return 0;
    }

    public static void onMethodCall(MethodCall call, MethodChannel.Result result, ViewerComponent component) {
        switch (call.method) {
            case FUNCTION_IMPORT_ANNOTATIONS: {
                checkFunctionPrecondition(component);
                String xfdf = call.argument(KEY_XFDF);
                try {
                    importAnnotations(xfdf, true, result, component);
                } catch (PDFNetException ex) {
                    ex.printStackTrace();
                    result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_MERGE_ANNOTATIONS: {
                checkFunctionPrecondition(component);
                String xfdf = call.argument(KEY_XFDF);
                try {
                    importAnnotations(xfdf, false, result, component);
                } catch (PDFNetException ex) {
                    ex.printStackTrace();
                    result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_EXPORT_ANNOTATIONS: {
                checkFunctionPrecondition(component);
                String annotationList = call.argument(KEY_ANNOTATION_LIST);
                try {
                    exportAnnotations(annotationList, result, component);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    result.error(Integer.toString(ex.hashCode()), "JSONException Error: " + ex, null);
                } catch (PDFNetException ex) {
                    ex.printStackTrace();
                    result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_FLATTEN_ANNOTATIONS: {
                checkFunctionPrecondition(component);
                Boolean formsOnly = call.argument(KEY_FORMS_ONLY);
                if (formsOnly != null) {
                    try {
                        flattenAnnotations(formsOnly, result, component);
                    } catch (PDFNetException ex) {
                        ex.printStackTrace();
                        result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                    }
                }
                break;
            }
            case FUNCTION_DELETE_ANNOTATIONS: {
                checkFunctionPrecondition(component);
                String annotationList = call.argument(KEY_ANNOTATION_LIST);
                try {
                    deleteAnnotations(annotationList, result, component);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    result.error(Integer.toString(ex.hashCode()), "JSONException Error: " + ex, null);
                } catch (PDFNetException ex) {
                    ex.printStackTrace();
                    result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_SELECT_ANNOTATION: {
                checkFunctionPrecondition(component);
                String annotation = call.argument(KEY_ANNOTATION);
                try {
                    selectAnnotation(annotation, result, component);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    result.error(Integer.toString(ex.hashCode()), "JSONException Error: " + ex, null);
                } catch (PDFNetException ex) {
                    ex.printStackTrace();
                    result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_SET_FLAGS_FOR_ANNOTATIONS: {
                checkFunctionPrecondition(component);
                String annotationsWithFlags = call.argument(KEY_ANNOTATIONS_WITH_FLAGS);
                try {
                    setFlagsForAnnotations(annotationsWithFlags, result, component);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    result.error(Integer.toString(ex.hashCode()), "JSONException Error: " + ex, null);
                } catch (PDFNetException ex) {
                    ex.printStackTrace();
                    result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_SET_PROPERTIES_FOR_ANNOTATION: {
                checkFunctionPrecondition(component);
                String annotation = call.argument(KEY_ANNOTATION);
                String properties = call.argument(KEY_ANNOTATION_PROPERTIES);
                try {
                    setPropertiesForAnnotation(annotation, properties, result, component);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    result.error(Integer.toString(ex.hashCode()), "JSONException Error: " + ex, null);
                } catch (PDFNetException ex) {
                    ex.printStackTrace();
                    result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_OPEN_ANNOTATION_LIST: {
                checkFunctionPrecondition(component);
                openAnnotationList(result, component);
                break;
            }
            case FUNCTION_IMPORT_ANNOTATION_COMMAND: {
                checkFunctionPrecondition(component);
                String xfdfCommand = call.argument(KEY_XFDF_COMMAND);
                try {
                    importAnnotationCommand(xfdfCommand, result, component);
                } catch (PDFNetException ex) {
                    ex.printStackTrace();
                    result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_IMPORT_BOOKMARK_JSON: {
                checkFunctionPrecondition(component);
                String bookmarkJson = call.argument(KEY_BOOKMARK_JSON);
                try {
                    importBookmarkJson(bookmarkJson, result, component);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    result.error(Integer.toString(ex.hashCode()), "JSONException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_SAVE_DOCUMENT: {
                checkFunctionPrecondition(component);
                saveDocument(result, component);
                break;
            }
            case FUNCTION_COMMIT_TOOL: {
                checkFunctionPrecondition(component);
                commitTool(result, component);
                break;
            }
            case FUNCTION_GET_PAGE_COUNT: {
                checkFunctionPrecondition(component);
                try {
                    getPageCount(result, component);
                } catch (PDFNetException ex) {
                    ex.printStackTrace();
                    result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_HANDLE_BACK_BUTTON: {
                checkFunctionPrecondition(component);
                handleBackButton(result, component);
                break;
            }
            case FUNCTION_GET_PAGE_CROP_BOX: {
                checkFunctionPrecondition(component);
                Integer pageNumber = call.argument(KEY_PAGE_NUMBER);
                if (pageNumber != null) {
                    try {
                        getPageCropBox(pageNumber, result, component);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                        result.error(Integer.toString(ex.hashCode()), "JSONException Error: " + ex, null);
                    } catch (PDFNetException ex) {
                        ex.printStackTrace();
                        result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                    }
                }
                break;
            }
            case FUNCTION_UNDO: {
                checkFunctionPrecondition(component);
                undo(result, component);
                break;
            }
            case FUNCTION_REDO: {
                checkFunctionPrecondition(component);
                redo(result, component);
                break;
            }
            case FUNCTION_CAN_UNDO: {
                checkFunctionPrecondition(component);
                canUndo(result, component);
                break;
            }
            case FUNCTION_CAN_REDO: {
                checkFunctionPrecondition(component);
                canRedo(result, component);
                break;
            }
            case FUNCTION_SET_CURRENT_PAGE: {
                checkFunctionPrecondition(component);
                Integer pageNumber = call.argument(KEY_PAGE_NUMBER);
                if (pageNumber != null) {
                    setCurrentPage(pageNumber, result, component);
                }
                break;
            }
            case FUNCTION_GET_DOCUMENT_PATH: {
                checkFunctionPrecondition(component);
                getDocumentPath(result, component);
                break;
            }
            case FUNCTION_SET_TOOL_MODE: {
                checkFunctionPrecondition(component);
                String toolModeString = call.argument(KEY_TOOL_MODE);
                setToolMode(toolModeString, result, component);
                break;
            }
            case FUNCTION_SET_FLAG_FOR_FIELDS: {
                checkFunctionPrecondition(component);
                ArrayList<String> fieldNames = call.argument(KEY_FIELD_NAMES);
                Integer flag = call.argument(KEY_FLAG);
                Boolean flagValue = call.argument(KEY_FLAG_VALUE);
                if (fieldNames != null && flag != null && flagValue != null) {
                    try {
                        setFlagForFields(fieldNames, flag, flagValue, result, component);
                    } catch (PDFNetException ex) {
                        ex.printStackTrace();
                        result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                    }
                }
                break;
            }
            case FUNCTION_SET_VALUES_FOR_FIELDS: {
                checkFunctionPrecondition(component);
                String fieldsString = call.argument(KEY_FIELDS);
                if (fieldsString != null) {
                    try {
                        setValuesForFields(fieldsString, result, component);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                        result.error(Integer.toString(ex.hashCode()), "JSONException Error: " + ex, null);
                    } catch (PDFNetException ex) {
                        ex.printStackTrace();
                        result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                    }
                }
                break;
            }
            case FUNCTION_CLOSE_ALL_TABS: {
                checkFunctionPrecondition(component);
                closeAllTabs(result, component);
                break;
            }
            case FUNCTION_DELETE_ALL_ANNOTATIONS: {
                checkFunctionPrecondition(component);
                deleteAllAnnotations(result, component);
                break;
            }
            case FUNCTION_GET_PAGE_ROTATION: {
                checkFunctionPrecondition(component);
                Integer pageNumber = call.argument(KEY_PAGE_NUMBER);
                if (pageNumber != null) {
                    getPageRotation(pageNumber, result, component);
                }
                break;
            }
            case FUNCTION_ROTATE_CLOCKWISE: {
                checkFunctionPrecondition(component);
                rotateClockwise(result, component);
                break;
            }
            case FUNCTION_ROTATE_COUNTER_CLOCKWISE: {
                checkFunctionPrecondition(component);
                rotateCounterClockwise(result, component);
                break;
            }
            case FUNCTION_EXPORT_AS_IMAGE: {
                checkFunctionPrecondition(component);
                Integer pageNumber = call.argument(KEY_PAGE_NUMBER);
                Integer dpi = call.argument(KEY_DPI);
                String exportFormat = call.argument(KEY_EXPORT_FORMAT);
                if (pageNumber != null && dpi != null && exportFormat != null) {
                    exportAsImage(pageNumber, dpi, exportFormat, result, component);
                }
                break;
            }
            case FUNCTION_EXPORT_AS_IMAGE_FROM_FILE_PATH: {
                // Static, doesn't require viewer.
                Integer pageNumber = call.argument(KEY_PAGE_NUMBER);
                Integer dpi = call.argument(KEY_DPI);
                String exportFormat = call.argument(KEY_EXPORT_FORMAT);
                String path = call.argument(KEY_PATH);
                if (pageNumber != null && dpi != null && exportFormat != null && path != null) {
                    exportAsImageFromFilePath(pageNumber, dpi, exportFormat, path, result, component);
                }
                break;
            }
            case FUNCTION_GO_TO_PREVIOUS_PAGE: {
                checkFunctionPrecondition(component);
                gotoPreviousPage(result, component);
                break;
            }
            case FUNCTION_GO_TO_NEXT_PAGE: {
                checkFunctionPrecondition(component);
                gotoNextPage(result, component);
                break;
            }
            case FUNCTION_GO_TO_FIRST_PAGE: {
                checkFunctionPrecondition(component);
                gotoFirstPage(result, component);
                break;
            }
            case FUNCTION_GO_TO_LAST_PAGE: {
                checkFunctionPrecondition(component);
                gotoLastPage(result, component);
                break;
            }
            case FUNCTION_ADD_BOOKMARK: {
                checkFunctionPrecondition(component);
                String title = call.argument(KEY_TITLE);
                Integer pageNumber = call.argument(KEY_PAGE_NUMBER);
                if (title != null && pageNumber != null) {
                    addBookmark(title, pageNumber, result, component);
                }
                break;
            }
            case FUNCTION_OPEN_BOOKMARK_LIST: {
                checkFunctionPrecondition(component);
                openBookmarkList(result, component);
                break;
            }
            case FUNCTION_OPEN_LAYERS_LIST: {
                checkFunctionPrecondition(component);
                openLayersList(result, component);
                break;
            }
            case FUNCTION_OPEN_OUTLINE_LIST: {
                checkFunctionPrecondition(component);
                openOutlineList(result, component);
                break;
            }
            case FUNCTION_OPEN_THUMBNAILS_VIEW: {
                checkFunctionPrecondition(component);
                openThumbnailsView(result, component);
                break;
            }
            case FUNCTION_OPEN_ROTATE_DIALOG: {
                checkFunctionPrecondition(component);
                openRotateDialog(result, component);
                break;
            }
            case FUNCTION_OPEN_ADD_PAGES_VIEW: {
                checkFunctionPrecondition(component);
                openAddPagesView(result, component);
                break;
            }
            case FUNCTION_OPEN_VIEW_SETTINGS: {
                checkFunctionPrecondition(component);
                openViewSettings(result, component);
                break;
            }
            case FUNCTION_OPEN_CROP: {
                checkFunctionPrecondition(component);
                openCrop(result, component);
                break;
            }
            case FUNCTION_OPEN_MANUAL_CROP: {
                checkFunctionPrecondition(component);
                openManualCrop(result, component);
                break;
            }
            case FUNCTION_OPEN_SEARCH: {
                checkFunctionPrecondition(component);
                openSearch(result, component);
                break;
            }
            case FUNCTION_OPEN_TAB_SWITCHER: {
                checkFunctionPrecondition(component);
                openTabSwitcher(result, component);
                break;
            }
            case FUNCTION_OPEN_GO_TO_PAGE_VIEW: {
                checkFunctionPrecondition(component);
                openGoToPageView(result, component);
                break;
            }
            case FUNCTION_OPEN_NAVIGATION_LISTS: {
                checkFunctionPrecondition(component);
                openNavigationLists(result, component);
                break;
            }
            case FUNCTION_GET_CURRENT_PAGE: {
                checkFunctionPrecondition(component);
                getCurrentPage(result, component);
                break;
            }
            case FUNCTION_GET_SAVED_SIGNATURES: {
                checkFunctionPrecondition(component);
                getSavedSignatures(result, component);
                break;
            }
            case FUNCTION_GET_SAVED_SIGNATURE_FOLDER: {
                checkFunctionPrecondition(component);
                getSavedSignatureFolder(result, component);
                break;
            }
            case FUNCTION_GET_SAVED_SIGNATURE_JPG_FOLDER: {
                checkFunctionPrecondition(component);
                getSavedSignatureJpgFolder(result, component);
                break;
            }
            case FUNCTION_GROUP_ANNOTATIONS: {
                checkFunctionPrecondition(component);
                try {
                    groupAnnotations(call, result, component);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    result.error(Integer.toString(ex.hashCode()), "JSONException Error: " + ex, null);
                } catch (PDFNetException ex) {
                    ex.printStackTrace();
                    result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_UNGROUP_ANNOTATIONS: {
                checkFunctionPrecondition(component);
                try {
                    ungroupAnnotations(call, result, component);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    result.error(Integer.toString(ex.hashCode()), "JSONException Error: " + ex, null);
                } catch (PDFNetException ex) {
                    ex.printStackTrace();
                    result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_START_SEARCH_MODE: {
                checkFunctionPrecondition(component);
                try {
                    startSearchMode(call, result, component);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    result.error(Integer.toString(ex.hashCode()), "JSONException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_EXIT_SEARCH_MODE: {
                checkFunctionPrecondition(component);
                exitSearchMode(result, component);
                break;
            }
            case FUNCTION_ZOOM_WITH_CENTER: {
                checkFunctionPrecondition(component);
                zoomWithCenter(call, result, component);
                break;
            }
            case FUNCTION_ZOOM_TO_RECT: {
                checkFunctionPrecondition(component);
                zoomToRect(call, result, component);
                break;
            }
            case FUNCTION_GET_ZOOM: {
                checkFunctionPrecondition(component);
                getZoom(result, component);
                break;
            }
            case FUNCTION_SET_ZOOM_LIMITS: {
                checkFunctionPrecondition(component);
                try {
                    setZoomLimits(call, result, component);
                } catch (PDFNetException ex) {
                    ex.printStackTrace();
                    result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_SET_BACKGROUND_COLOR: {
                checkFunctionPrecondition(component);
                try {
                    setBackgroundColor(call, result, component);
                } catch (PDFNetException ex) {
                    ex.printStackTrace();
                    result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_SET_DEFAULT_PAGE_COLOR: {
                checkFunctionPrecondition(component);
                try {
                    setDefaultPageColor(call, result, component);
                } catch (PDFNetException ex) {
                    ex.printStackTrace();
                    result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_GET_SCROLL_POS: {
                checkFunctionPrecondition(component);
                try {
                    getScrollPos(result, component);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    result.error(Integer.toString(ex.hashCode()), "JSONException Error: " + ex, null);
                }
                break;
            }
            case FUNCTION_SET_HORIZONTAL_SCROLL_POSITION: {
                checkFunctionPrecondition(component);
                setHorizontalScrollPosition(call, result, component);
                break;
            }
            case FUNCTION_SET_VERTICAL_SCROLL_POSITION: {
                checkFunctionPrecondition(component);
                setVerticalScrollPosition(call, result, component);
                break;
            }
            case FUNCTION_SMART_ZOOM: {
                checkFunctionPrecondition(component);
                smartZoom(call, result, component);
                break;
            }
            case FUNCTION_GET_VISIBLE_PAGES: {
                checkFunctionPrecondition(component);
                getVisiblePages(result, component);
                break;
            }
            // Hygen Generated Method Cases
            case FUNCTION_SET_LAYOUT_MODE: {
                checkFunctionPrecondition(component);
                setLayoutMode(call, result, component);
                break;
            }
            case FUNCTION_SET_FIT_MODE: {
                checkFunctionPrecondition(component);
                setFitMode(call, result, component);
                break;
            }
            case FUNCTION_GET_ANNOTATIONS_ON_PAGE: {
                checkFunctionPrecondition(component);
                getAnnotationsOnPage(call, result, component);
                break;
            }
            // Hatn
            case FUNCTION_SET_THEME_MODE: {
                checkFunctionPrecondition(component);
                Boolean isDarkMode = call.argument(KEY_IS_DARK_MODE);
                if (isDarkMode != null) {
                    try {
                        setThemeMode(component, isDarkMode);
                    } catch (PDFNetException ex) {
                        ex.printStackTrace();
                        result.error(Integer.toString(ex.hashCode()), "JSONException Error: " + ex, null);
                    }
                }
                break;
            }
            default:
                Log.e("PDFTronFlutter", "notImplemented: " + call.method);
                result.notImplemented();
                break;
        }
    }

    // Methods

    private static void smartZoom(MethodCall call, MethodChannel.Result result, ViewerComponent component) {
        int x = call.argument(KEY_X);
        int y = call.argument(KEY_Y);
        boolean animated = call.argument(KEY_ANIMATED);
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (null == pdfViewCtrl) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }
        pdfViewCtrl.smartZoom(x, y, animated);
        result.success(null);
    }

    private static void setZoomLimits(MethodCall call, MethodChannel.Result result, ViewerComponent component)
            throws PDFNetException {
        String mode = call.argument(KEY_ZOOM_LIMIT_MODE);
        double minimum = call.argument(KEY_MINIMUM);
        double maximum = call.argument(KEY_MAXIMUM);
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (null == pdfViewCtrl) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }
        PDFViewCtrl.ZoomLimitMode limitMode = null;

        switch (mode) {
            case KEY_ZOOM_LIMIT_MODE_ABSOLUTE:
                limitMode = PDFViewCtrl.ZoomLimitMode.ABSOLUTE;
                break;
            case KEY_ZOOM_LIMIT_MODE_RELATIVE:
                limitMode = PDFViewCtrl.ZoomLimitMode.RELATIVE;
                break;
            case KEY_ZOOM_LIMIT_MODE_NONE:
                limitMode = PDFViewCtrl.ZoomLimitMode.NONE;
                break;
        }
        if (limitMode != null) {
            try {
                pdfViewCtrl.setZoomLimits(limitMode, minimum, maximum);
                result.success(null);
            } catch (PDFNetException ex) {
                ex.printStackTrace();
                result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
            }
        } else {
            result.error("InvalidState", "Invalid zoom limit mode", null);
        }
    }

    private static void ungroupAnnotations(MethodCall call, MethodChannel.Result result, ViewerComponent component) throws PDFNetException, JSONException {
        String annotationList = call.argument(KEY_ANNOTATION_LIST);
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        PDFDoc pdfDoc = component.getPdfDoc();

        if (null == pdfViewCtrl || null == pdfDoc) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        boolean shouldUnlock = false;
        try {
            pdfViewCtrl.docLock(true);
            shouldUnlock = true;

            if (null != annotationList) {
                JSONArray annotationJsonArray = new JSONArray(annotationList);
                ArrayList<Annot> validAnnotations = new ArrayList<>(annotationJsonArray.length());
                for (int i = 0; i < annotationJsonArray.length(); i++) {
                    JSONObject currAnnot = annotationJsonArray.getJSONObject(i);
                    if (currAnnot != null) {
                        String currAnnotId = currAnnot.getString(KEY_ANNOTATION_ID);
                        int currAnnotPageNumber = currAnnot.getInt(KEY_PAGE_NUMBER);
                        if (!Utils.isNullOrEmpty(currAnnotId)) {
                            Annot annotation = ViewerUtils.getAnnotById(pdfViewCtrl, currAnnotId, currAnnotPageNumber);
                            if (annotation != null && annotation.isValid()) {
                                validAnnotations.add(annotation);
                            }
                        }
                    }
                }
                AnnotUtils.ungroupAnnotations(pdfViewCtrl, validAnnotations);
            }
        } finally {
            if (shouldUnlock) {
                pdfViewCtrl.docUnlock();
            }
        }
    }

    private static void groupAnnotations(MethodCall call, MethodChannel.Result result, ViewerComponent component) throws PDFNetException, JSONException {
        String annotation = call.argument(KEY_ANNOTATION);
        String annotationList = call.argument(KEY_ANNOTATION_LIST);
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        PDFDoc pdfDoc = component.getPdfDoc();

        if (null == pdfViewCtrl || null == pdfDoc) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        boolean shouldUnlock = false;
        try {
            pdfViewCtrl.docLock(true);
            shouldUnlock = true;

            if (null != annotationList && annotation != null) {

                JSONObject annotationJson = new JSONObject(annotation);
                String primaryAnnotationId = annotationJson.getString(KEY_ANNOTATION_ID);
                int primaryAnnotPageNum = annotationJson.getInt(KEY_PAGE_NUMBER);
                Annot primaryAnnotation = ViewerUtils.getAnnotById(pdfViewCtrl, primaryAnnotationId, primaryAnnotPageNum);

                if (primaryAnnotation == null || !primaryAnnotation.isValid()) {
                    result.error("InvalidState", "Thew primary annotation cannot be null", null);
                    return;
                }

                JSONArray annotationJsonArray = new JSONArray(annotationList);
                ArrayList<Annot> allAnnotations = new ArrayList<>(annotationJsonArray.length());
                boolean containsPrimary = false;
                for (int i = 0; i < annotationJsonArray.length(); i++) {
                    JSONObject currAnnot = annotationJsonArray.getJSONObject(i);
                    if (currAnnot != null) {
                        String currAnnotId = currAnnot.getString(KEY_ANNOTATION_ID);
                        int currAnnotPageNumber = currAnnot.getInt(KEY_PAGE_NUMBER);
                        if (!Utils.isNullOrEmpty(currAnnotId)) {
                            Annot validAnnotation = ViewerUtils.getAnnotById(pdfViewCtrl, currAnnotId, currAnnotPageNumber);
                            if (validAnnotation != null && validAnnotation.isValid()) {
                                allAnnotations.add(validAnnotation);
                                if (!containsPrimary) {
                                    containsPrimary = currAnnotId.equals(primaryAnnotationId);
                                }
                            }
                        }
                    }
                }
                if (!containsPrimary && primaryAnnotation != null) {
                    allAnnotations.add(primaryAnnotation);
                }
                AnnotUtils.createAnnotationGroup(pdfViewCtrl, primaryAnnotation, allAnnotations);
            }
        } finally {
            if (shouldUnlock) {
                pdfViewCtrl.docUnlock();
            }
        }
    }

    private static void importAnnotations(String xfdf, boolean replace, MethodChannel.Result result, ViewerComponent component) throws PDFNetException {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        PDFDoc pdfDoc = component.getPdfDoc();
        if (null == pdfViewCtrl || null == pdfDoc || null == xfdf) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }
        boolean shouldUnlockRead = false;
        try {
            pdfViewCtrl.docLockRead();
            shouldUnlockRead = true;

            if (pdfDoc.hasDownloader()) {
                // still downloading file, let's wait for next call
                result.error("InvalidState", "Document download in progress, try again later", null);
                return;
            }
        } finally {
            if (shouldUnlockRead) {
                pdfViewCtrl.docUnlockRead();
            }
        }

        boolean shouldUnlock = false;
        try {
            pdfViewCtrl.docLock(true);
            shouldUnlock = true;

            FDFDoc fdfDoc = FDFDoc.createFromXFDF(xfdf);

            if (replace) {
                pdfDoc.fdfUpdate(fdfDoc);
            } else {
                pdfDoc.fdfMerge(fdfDoc);
            }
            pdfDoc.refreshAnnotAppearances();
            pdfViewCtrl.update(true);

            result.success(null);
        } finally {
            if (shouldUnlock) {
                pdfViewCtrl.docUnlock();
            }
        }
    }

    private static void exportAnnotations(String annotationList, MethodChannel.Result result, ViewerComponent component) throws PDFNetException, JSONException {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();

        if (null == pdfViewCtrl) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        boolean shouldUnlockRead = false;
        try {
            pdfViewCtrl.docLockRead();
            shouldUnlockRead = true;

            PDFDoc pdfDoc = pdfViewCtrl.getDoc();
            if (null == annotationList) {
                FDFDoc fdfDoc = pdfDoc.fdfExtract(PDFDoc.e_both);
                result.success(fdfDoc.saveAsXFDF());
            } else {
                JSONArray annotationJsonArray = new JSONArray(annotationList);
                ArrayList<Annot> validAnnotationList = new ArrayList<>(annotationJsonArray.length());
                for (int i = 0; i < annotationJsonArray.length(); i++) {
                    JSONObject currAnnot = annotationJsonArray.getJSONObject(i);
                    if (currAnnot != null) {
                        String currAnnotId = currAnnot.getString(KEY_ANNOTATION_ID);
                        int currAnnotPageNumber = currAnnot.getInt(KEY_PAGE_NUMBER);
                        if (!Utils.isNullOrEmpty(currAnnotId)) {
                            Annot validAnnotation = ViewerUtils.getAnnotById(pdfViewCtrl, currAnnotId, currAnnotPageNumber);
                            if (validAnnotation != null && validAnnotation.isValid()) {
                                validAnnotationList.add(validAnnotation);
                            }
                        }
                    }
                }

                if (validAnnotationList.size() > 0) {
                    FDFDoc fdfDoc = pdfDoc.fdfExtract(validAnnotationList);
                    result.success(fdfDoc.saveAsXFDF());
                } else {
                    result.success("");
                }
            }
        } finally {
            if (shouldUnlockRead) {
                pdfViewCtrl.docUnlockRead();
            }
        }
    }

    private static void flattenAnnotations(boolean formsOnly, MethodChannel.Result result, ViewerComponent component) throws PDFNetException {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        PDFDoc pdfDoc = component.getPdfDoc();
        if (null == pdfViewCtrl || null == pdfDoc) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }
        if (pdfViewCtrl.getToolManager() instanceof ToolManager) {
            ToolManager toolManager = (ToolManager) pdfViewCtrl.getToolManager();
            toolManager.setTool(toolManager.createTool(ToolManager.ToolMode.PAN, toolManager.getTool()));
        }

        boolean shouldUnlock = false;
        try {
            pdfViewCtrl.docLock(true);
            shouldUnlock = true;

            pdfDoc.flattenAnnotations(formsOnly);
        } finally {
            if (shouldUnlock) {
                pdfViewCtrl.docUnlock();
            }
            result.success(null);
        }
    }

    private static void deleteAnnotations(String annotationList, MethodChannel.Result result, ViewerComponent component) throws PDFNetException, JSONException {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        PDFDoc pdfDoc = component.getPdfDoc();

        if (null == pdfViewCtrl || null == pdfDoc) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        ToolManager toolManager = (ToolManager) pdfViewCtrl.getToolManager();

        JSONArray annotationJsonArray = new JSONArray(annotationList);

        for (int i = 0; i < annotationJsonArray.length(); i++) {
            JSONObject currAnnot = annotationJsonArray.getJSONObject(i);

            if (currAnnot != null) {
                String currAnnotId = currAnnot.getString(KEY_ANNOTATION_ID);
                int currAnnotPageNumber = currAnnot.getInt(KEY_PAGE_NUMBER);

                if (!Utils.isNullOrEmpty(currAnnotId)) {
                    Annot validAnnotation = ViewerUtils.getAnnotById(pdfViewCtrl, currAnnotId, currAnnotPageNumber);

                    if (validAnnotation != null && validAnnotation.isValid()) {
                        boolean shouldUnlock = false;

                        try {
                            pdfViewCtrl.docLock(true);
                            shouldUnlock = true;
                            HashMap<Annot, Integer> map = new HashMap<>(1);
                            map.put(validAnnotation, currAnnotPageNumber);
                            toolManager.raiseAnnotationsPreRemoveEvent(map);

                            Page page = pdfViewCtrl.getDoc().getPage(currAnnotPageNumber);
                            page.annotRemove(validAnnotation);
                            pdfViewCtrl.update(validAnnotation, currAnnotPageNumber);

                            toolManager.raiseAnnotationsRemovedEvent(map);
                        } finally {
                            if (shouldUnlock) {
                                pdfViewCtrl.docUnlock();
                            }
                        }
                        toolManager.deselectAll();
                    }
                }
            }
        }
    }

    private static void selectAnnotation(String annotation, MethodChannel.Result result, ViewerComponent component) throws PDFNetException, JSONException {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        PDFDoc pdfDoc = component.getPdfDoc();

        if (null == pdfViewCtrl || null == pdfDoc) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        JSONObject annotationJson = new JSONObject(annotation);

        ToolManager toolManager = (ToolManager) pdfViewCtrl.getToolManager();

        String annotationId = annotationJson.getString(KEY_ANNOTATION_ID);
        int annotationPageNumber = annotationJson.getInt(KEY_PAGE_NUMBER);

        if (!Utils.isNullOrEmpty(annotationId)) {
            toolManager.selectAnnot(annotationId, annotationPageNumber);
        }
    }

    private static void openAnnotationList(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabHostFragment2 pdfViewCtrlTabHostFragment2 = component.getPdfViewCtrlTabHostFragment();
        if (pdfViewCtrlTabHostFragment2 == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        if (isBookmarkListVisible) {
            if (isOutlineListVisible) {
                if (isAnnotationListVisible) {
                    pdfViewCtrlTabHostFragment2.onOutlineOptionSelected(2);
                }
            } else {
                if (isAnnotationListVisible) {
                    pdfViewCtrlTabHostFragment2.onOutlineOptionSelected(1);
                }
            }
        } else {
            if (isOutlineListVisible) {
                if (isAnnotationListVisible) {
                    pdfViewCtrlTabHostFragment2.onOutlineOptionSelected(1);
                }
            } else {
                if (isAnnotationListVisible) {
                    pdfViewCtrlTabHostFragment2.onOutlineOptionSelected(0);
                }
            }
        }
    }

    private static void openBookmarkList(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabHostFragment2 pdfViewCtrlTabHostFragment2 = component.getPdfViewCtrlTabHostFragment();
        if (pdfViewCtrlTabHostFragment2 == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        if (isBookmarkListVisible) {
            component.getPdfViewCtrlTabHostFragment().onOutlineOptionSelected(0);
        }
    }

    private static void openOutlineList(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabHostFragment2 pdfViewCtrlTabHostFragment2 = component.getPdfViewCtrlTabHostFragment();
        if (pdfViewCtrlTabHostFragment2 == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        if (isBookmarkListVisible) {
            pdfViewCtrlTabHostFragment2.onOutlineOptionSelected(1);
        } else {
            pdfViewCtrlTabHostFragment2.onOutlineOptionSelected(0);
        }
    }

    private static void openLayersList(MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        PdfLayerDialog pdfLayerDialog = new PdfLayerDialog(pdfViewCtrl.getContext(), pdfViewCtrl);
        pdfLayerDialog.show();
    }

    private static void openThumbnailsView(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabHostFragment2 pdfViewCtrlTabHostFragment2 = component.getPdfViewCtrlTabHostFragment();
        if (pdfViewCtrlTabHostFragment2 == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        pdfViewCtrlTabHostFragment2.onPageThumbnailOptionSelected(false, null);
        result.success(null);
    }

    private static void openRotateDialog(MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        PdfViewCtrlTabHostFragment2 pdfViewCtrlTabHostFragment2 = component.getPdfViewCtrlTabHostFragment();
        if (pdfViewCtrl != null && pdfViewCtrlTabHostFragment2 != null) {
            RotateDialogFragment.newInstance()
                    .setPdfViewCtrl(pdfViewCtrl)
                    .show(pdfViewCtrlTabHostFragment2.getChildFragmentManager(), "rotate_dialog");
            result.success(null);
            return;
        }

        result.error("InvalidState", "Activity not attached", null);
    }

    private static void openAddPagesView(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabHostFragment2 pdfViewCtrlTabHostFragment2 = component.getPdfViewCtrlTabHostFragment();
        if (pdfViewCtrlTabHostFragment2 == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        pdfViewCtrlTabHostFragment2.addNewPage();
        result.success(null);
    }

    private static void openViewSettings(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabHostFragment2 pdfViewCtrlTabHostFragment2 = component.getPdfViewCtrlTabHostFragment();
        if (pdfViewCtrlTabHostFragment2 == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        pdfViewCtrlTabHostFragment2.onViewModeOptionSelected();
        result.success(null);
    }

    private static void openCrop(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabHostFragment2 pdfViewCtrlTabHostFragment2 = component.getPdfViewCtrlTabHostFragment();
        if (pdfViewCtrlTabHostFragment2 == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        pdfViewCtrlTabHostFragment2.onViewModeSelected(
                PdfViewCtrlSettingsManager.KEY_PREF_VIEWMODE_USERCROP_VALUE);
        result.success(null);
    }

    private static void openManualCrop(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabHostFragment2 pdfViewCtrlTabHostFragment2 = component.getPdfViewCtrlTabHostFragment();
        if (pdfViewCtrlTabHostFragment2 == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        pdfViewCtrlTabHostFragment2.onUserCropMethodSelected(UserCropSelectionDialogFragment.MODE_MANUAL_CROP);
        result.success(null);
    }

    private static void openSearch(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabHostFragment2 pdfViewCtrlTabHostFragment2 = component.getPdfViewCtrlTabHostFragment();
        if (pdfViewCtrlTabHostFragment2 == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        pdfViewCtrlTabHostFragment2.onSearchOptionSelected();
        result.success(null);
    }

    private static void openTabSwitcher(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabHostFragment2 pdfViewCtrlTabHostFragment2 = component.getPdfViewCtrlTabHostFragment();
        if (pdfViewCtrlTabHostFragment2 == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        pdfViewCtrlTabHostFragment2.onOpenTabSwitcher();
        result.success(null);
    }

    private static void openGoToPageView(MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        final PdfViewCtrlTabFragment2 pdfViewCtrlTabFragment = component.getPdfViewCtrlTabFragment();
        if (pdfViewCtrl == null || pdfViewCtrlTabFragment == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        DialogGoToPage dlgGotoPage = new DialogGoToPage(pdfViewCtrl.getContext(), pdfViewCtrl, new DialogGoToPage.DialogGoToPageListener() {
            @Override
            public void onPageSet(int pageNum) {
                pdfViewCtrlTabFragment.setCurrentPageHelper(pageNum, true);
                if (pdfViewCtrlTabFragment.getReflowControl() != null) {
                    try {
                        pdfViewCtrlTabFragment.getReflowControl().setCurrentPage(pageNum);
                    } catch (Exception e) {
                        AnalyticsHandlerAdapter.getInstance().sendException(e);
                    }
                }
            }
        });
        dlgGotoPage.show();
        result.success(null);
    }

    private static void openNavigationLists(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabHostFragment2 pdfViewCtrlTabHostFragment2 = component.getPdfViewCtrlTabHostFragment();
        if (pdfViewCtrlTabHostFragment2 == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        pdfViewCtrlTabHostFragment2.onOutlineOptionSelected();
    }

    private static void getCurrentPage(MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        result.success(pdfViewCtrl.getCurrentPage());
    }

    private static void getZoom(MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }
        double zoom = pdfViewCtrl.getZoom();
        result.success(zoom);
    }

    private static void getSavedSignatures(MethodChannel.Result result, @NonNull ViewerComponent component) {
        List<String> signatures = new ArrayList<String>();
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        Context context = pdfViewCtrl.getContext();
        if (context != null) {
            File[] files = StampManager.getInstance().getSavedSignatures(context);
            for (int i = 0; i < files.length; i++) {
                signatures.add(files[i].getAbsolutePath());
            }
        }
        result.success(signatures);
    }

    private static void getSavedSignatureFolder(MethodChannel.Result result, @NonNull ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }
        String filePath = "";
        Context context = pdfViewCtrl.getContext();
        if (context != null) {
            File file = StampManager.getInstance().getSavedSignatureFolder(context);
            filePath = file.getAbsolutePath();
        }
        result.success(filePath);
    }

    private static void getSavedSignatureJpgFolder(MethodChannel.Result result, @NonNull ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }
        String filePath = "";
        Context context = pdfViewCtrl.getContext();
        if (context != null) {
            File file = StampManager.getInstance().getSavedSignatureJpgFolder(context);
            filePath = file.getAbsolutePath();
        }
        result.success(filePath);
    }

    private static void setFlagsForAnnotations(String annotationsWithFlags, MethodChannel.Result result,
            ViewerComponent component) throws PDFNetException, JSONException {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        PDFDoc pdfDoc = component.getPdfDoc();
        ToolManager toolManager = component.getToolManager();

        if (null == pdfViewCtrl || null == pdfDoc || null == toolManager) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        JSONArray annotationWithFlagsArray = new JSONArray(annotationsWithFlags);

        boolean shouldUnlock = false;
        try {
            pdfViewCtrl.docLock(true);
            shouldUnlock = true;

            // for each annotation
            for (int i = 0; i < annotationWithFlagsArray.length(); i++) {
                JSONObject currentAnnotationWithFlags = annotationWithFlagsArray.getJSONObject(i);

                JSONObject currentAnnotation = getJSONObjectFromJSONObject(currentAnnotationWithFlags, KEY_ANNOTATION);
                String currentAnnotationId = currentAnnotation.getString(KEY_ANNOTATION_ID);
                int currentAnnotationPageNumber = currentAnnotation.getInt(KEY_PAGE_NUMBER);

                if (!Utils.isNullOrEmpty(currentAnnotationId)) {
                    Annot validAnnotation = ViewerUtils.getAnnotById(pdfViewCtrl, currentAnnotationId, currentAnnotationPageNumber);

                    if (validAnnotation == null || !validAnnotation.isValid()) {
                        continue;
                    }

                    JSONArray currentFlagArray = getJSONArrayFromJSONObject(currentAnnotationWithFlags, KEY_ANNOTATION_FLAG_LISTS);

                    // for each flag
                    for (int j = 0; j < currentFlagArray.length(); j++) {
                        JSONObject currentFlagObject = currentFlagArray.getJSONObject(j);
                        String currentFlag = currentFlagObject.getString(KEY_ANNOTATION_FLAG);
                        boolean currentFlagValue = currentFlagObject.getBoolean(KEY_ANNOTATION_FLAG_VALUE);

                        if (currentFlag == null) {
                            continue;
                        }

                        int flagNumber = -1;
                        switch (currentFlag) {
                            case ANNOTATION_FLAG_HIDDEN:
                                flagNumber = Annot.e_hidden;
                                break;
                            case ANNOTATION_FLAG_INVISIBLE:
                                flagNumber = Annot.e_invisible;
                                break;
                            case ANNOTATION_FLAG_LOCKED:
                                flagNumber = Annot.e_locked;
                                break;
                            case ANNOTATION_FLAG_LOCKED_CONTENTS:
                                flagNumber = Annot.e_locked_contents;
                                break;
                            case ANNOTATION_FLAG_NO_ROTATE:
                                flagNumber = Annot.e_no_rotate;
                                break;
                            case ANNOTATION_FLAG_NO_VIEW:
                                flagNumber = Annot.e_no_view;
                                break;
                            case ANNOTATION_FLAG_NO_ZOOM:
                                flagNumber = Annot.e_no_zoom;
                                break;
                            case ANNOTATION_FLAG_PRINT:
                                flagNumber = Annot.e_print;
                                break;
                            case ANNOTATION_FLAG_READ_ONLY:
                                flagNumber = Annot.e_read_only;
                                break;
                            case ANNOTATION_FLAG_TOGGLE_NO_VIEW:
                                flagNumber = Annot.e_toggle_no_view;
                                break;
                        }
                        if (flagNumber != -1) {

                            HashMap<Annot, Integer> map = new HashMap<>(1);
                            map.put(validAnnotation, currentAnnotationPageNumber);
                            toolManager.raiseAnnotationsPreModifyEvent(map);

                            validAnnotation.setFlag(flagNumber, currentFlagValue);
                            pdfViewCtrl.update(validAnnotation, currentAnnotationPageNumber);

                            toolManager.raiseAnnotationsModifiedEvent(map, Tool.getAnnotationModificationBundle(null));
                        }
                    }
                }
            }
        } finally {
            if (shouldUnlock) {
                pdfViewCtrl.docUnlock();
            }
        }
    }

    private static boolean isValidJSONValue(JSONObject json, String key) throws JSONException {
        if (json.isNull(key)) {
            return false;
        }
        Object value = json.get(key);
        if (value instanceof String) {
            return !"null".equals((String) value);
        }
        return true;
    }

    private static void setPropertiesForAnnotation(String annotation, String properties, MethodChannel.Result result, ViewerComponent component) throws PDFNetException, JSONException {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        ToolManager toolManager = component.getToolManager();

        if (null == pdfViewCtrl || null == toolManager) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        JSONObject annotationJson = new JSONObject(annotation);

        String annotationId = annotationJson.getString(KEY_ANNOTATION_ID);
        int annotationPageNumber = annotationJson.getInt(KEY_PAGE_NUMBER);

        JSONObject propertiesJson = new JSONObject(properties);

        boolean shouldUnlock = false;
        try {
            pdfViewCtrl.docLock(true);
            shouldUnlock = true;

            Annot annot = ViewerUtils.getAnnotById(pdfViewCtrl, annotationId, annotationPageNumber);
            if (annot != null && annot.isValid()) {

                HashMap<Annot, Integer> map = new HashMap<>(1);
                map.put(annot, annotationPageNumber);
                toolManager.raiseAnnotationsPreModifyEvent(map);

                if (isValidJSONValue(propertiesJson, KEY_CONTENTS)) {
                    Object contents = propertiesJson.get(KEY_CONTENTS);
                    if (contents instanceof String) {
                        annot.setContents((String) contents);
                    }
                }

                if (isValidJSONValue(propertiesJson, KEY_RECT)) {
                    Object object = propertiesJson.get(KEY_RECT);
                    com.pdftron.pdf.Rect pdfRect = getRectFromObject(object);
                    if (pdfRect != null) {
                        annot.setRect(pdfRect);
                    }
                }

                if (isValidJSONValue(propertiesJson, KEY_ROTATION)) {
                    Object object = propertiesJson.get(KEY_ROTATION);
                    if (object instanceof Integer) {
                        annot.setRotation((Integer) object);
                        annot.refreshAppearance();
                    }
                }

                if (annot.isMarkup()) {
                    Markup markupAnnot = new Markup(annot);

                    if (isValidJSONValue(propertiesJson, KEY_SUBJECT)) {
                        Object subject = propertiesJson.get(KEY_SUBJECT);
                        if (subject instanceof String) {
                            markupAnnot.setSubject((String) subject);
                        }
                    }

                    if (isValidJSONValue(propertiesJson, KEY_TITLE)) {
                        Object title = propertiesJson.get(KEY_TITLE);
                        if (title instanceof String) {
                            markupAnnot.setTitle((String) title);
                        }
                    }

                    if (isValidJSONValue(propertiesJson, KEY_CONTENT_RECT)) {
                        Object object = propertiesJson.get(KEY_CONTENT_RECT);
                        com.pdftron.pdf.Rect pdfRect = getRectFromObject(object);
                        if (pdfRect != null) {
                            markupAnnot.setContentRect(pdfRect);
                        }
                    }
                }

                pdfViewCtrl.update(annot, annotationPageNumber);

                toolManager.raiseAnnotationsModifiedEvent(map, Tool.getAnnotationModificationBundle(null));
            }
        } finally {
            if (shouldUnlock) {
                pdfViewCtrl.docUnlock();
            }
        }
    }

    private static void importAnnotationCommand(String xfdfCommand, MethodChannel.Result result, ViewerComponent component) throws PDFNetException {
        ToolManager toolManager = component.getToolManager();
        if (toolManager != null && toolManager.getAnnotManager() != null) {
            toolManager.getAnnotManager().onRemoteChange(xfdfCommand);
            result.success(null);
        } else {
            PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
            PDFDoc pdfDoc = component.getPdfDoc();
            if (null == pdfViewCtrl || null == pdfDoc || null == xfdfCommand) {
                result.error("InvalidState", "Activity not attached", null);
                return;
            }
            boolean shouldUnlockRead = false;
            try {
                pdfViewCtrl.docLockRead();
                shouldUnlockRead = true;

                if (pdfDoc.hasDownloader()) {
                    // still downloading file, let's wait for next call
                    result.error("InvalidState", "Document download in progress, try again later", null);
                    return;
                }
            } finally {
                if (shouldUnlockRead) {
                    pdfViewCtrl.docUnlockRead();
                }
            }

            boolean shouldUnlock = false;
            try {
                pdfViewCtrl.docLock(true);
                shouldUnlock = true;

                FDFDoc fdfDoc = pdfDoc.fdfExtract(PDFDoc.e_both);
                String xfdf = fdfDoc.saveAsXFDF();
                FDFDoc newFdfDoc = FDFDoc.createFromXFDF(xfdf);
                newFdfDoc.mergeAnnots(xfdfCommand);

                pdfDoc.fdfUpdate(newFdfDoc);
                pdfDoc.refreshAnnotAppearances();
                pdfViewCtrl.update(true);
                result.success(null);
            } finally {
                if (shouldUnlock) {
                    pdfViewCtrl.docUnlock();
                }
            }
        }
    }

    private static void importBookmarkJson(String bookmarkJson, MethodChannel.Result result, ViewerComponent component) throws JSONException {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (null == pdfViewCtrl || null == bookmarkJson) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }
        BookmarkManager.importPdfBookmarks(pdfViewCtrl, bookmarkJson);
        PdfViewCtrlTabHostFragment2 hostFragment2 = component.getPdfViewCtrlTabHostFragment();
        if (hostFragment2 != null) {
            hostFragment2.reloadUserBookmarks();
        }
        result.success(null);
    }

    private static void saveDocument(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabFragment2 pdfViewCtrlTabFragment = component.getPdfViewCtrlTabFragment();
        if (pdfViewCtrlTabFragment != null) {
            pdfViewCtrlTabFragment.setSavingEnabled(true);
            pdfViewCtrlTabFragment.save(false, true, true);
            pdfViewCtrlTabFragment.setSavingEnabled(component.isAutoSaveEnabled());

            if (component.isBase64()) {
                try {
                    byte[] data = FileUtils.readFileToByteArray(pdfViewCtrlTabFragment.getFile());
                    result.success(Base64.encodeToString(data, Base64.DEFAULT));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    result.success("");
                }
            } else {
                result.success(pdfViewCtrlTabFragment.getFilePath());
            }
            return;
        }
        result.error("InvalidState", "Activity not attached", null);
    }

    private static void commitTool(MethodChannel.Result result, ViewerComponent component) {
        ToolManager toolManager = component.getToolManager();
        if (toolManager != null) {
            ToolManager.Tool currentTool = toolManager.getTool();
            if (currentTool instanceof FreehandCreate) {
                ((FreehandCreate) currentTool).commitAnnotation();
                toolManager.setTool(toolManager.createTool(ToolManager.ToolMode.PAN, null));
                result.success(true);
            } else if (currentTool instanceof AdvancedShapeCreate) {
                ((AdvancedShapeCreate) currentTool).commit();
                toolManager.setTool(toolManager.createTool(ToolManager.ToolMode.PAN, null));
                result.success(true);
            }
            result.success(false);
            return;
        }
        result.error("InvalidState", "Tool manager not found", null);
    }

    private static void getPageCount(MethodChannel.Result result, ViewerComponent component) throws PDFNetException {
        PDFDoc pdfDoc = component.getPdfDoc();
        if (pdfDoc == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }
        result.success(pdfDoc.getPageCount());
    }

    private static void handleBackButton(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabHostFragment2 pdfViewCtrlTabHostFragment = component.getPdfViewCtrlTabHostFragment();
        if (pdfViewCtrlTabHostFragment == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        result.success(pdfViewCtrlTabHostFragment.handleBackPressed());
    }

    private static void getPageCropBox(int pageNumber, MethodChannel.Result result, ViewerComponent component) throws PDFNetException, JSONException {
        JSONObject jsonObject = new JSONObject();
        PDFDoc pdfDoc = component.getPdfDoc();
        if (pdfDoc == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }
        Rect rect = pdfDoc.getPage(pageNumber).getCropBox();
        jsonObject.put(KEY_X1, rect.getX1());
        jsonObject.put(KEY_Y1, rect.getY1());
        jsonObject.put(KEY_X2, rect.getX2());
        jsonObject.put(KEY_Y2, rect.getY2());
        jsonObject.put(KEY_WIDTH, rect.getWidth());
        jsonObject.put(KEY_HEIGHT, rect.getHeight());
        result.success(jsonObject.toString());
    }

    private static void undo(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabFragment2 pdfViewCtrlTabFragment = component.getPdfViewCtrlTabFragment();
        if (pdfViewCtrlTabFragment != null) {
            pdfViewCtrlTabFragment.undo();
            result.success(null);
        } else {
            result.error("InvalidState", "Activity not attached", null);
        }
    }

    private static void redo(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabFragment2 pdfViewCtrlTabFragment = component.getPdfViewCtrlTabFragment();
        if (pdfViewCtrlTabFragment != null) {
            pdfViewCtrlTabFragment.redo();
            result.success(null);
        } else {
            result.error("InvalidState", "Activity not attached", null);
        }
    }

    private static void canUndo(MethodChannel.Result result, ViewerComponent component) {
        ToolManager toolManager = component.getToolManager();
        if (toolManager != null && toolManager.getUndoRedoManger() != null) {
            result.success(toolManager.getUndoRedoManger().canUndo());
        } else {
            result.error("InvalidState", "Tool manager or undoRedo manager not found", null);
        }
    }

    private static void canRedo(MethodChannel.Result result, ViewerComponent component) {
        ToolManager toolManager = component.getToolManager();
        if (toolManager != null && toolManager.getUndoRedoManger() != null) {
            result.success(toolManager.getUndoRedoManger().canRedo());
        } else {
            result.error("InvalidState", "Tool manager or undoRedo manager not found", null);
        }
    }

    private static void setCurrentPage(int pageNumber, MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        result.success(pdfViewCtrl != null && pdfViewCtrl.setCurrentPage(pageNumber));
    }

    private static void getDocumentPath(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabFragment2 pdfViewCtrlTabFragment = component.getPdfViewCtrlTabFragment();
        if (pdfViewCtrlTabFragment != null) {
            result.success(pdfViewCtrlTabFragment.getFilePath());
        } else {
            result.error("InvalidState", "Activity not attached", null);
        }
    }

    private static void setFlagForFields(ArrayList<String> fieldNames, int flag, boolean flagValue, MethodChannel.Result result, ViewerComponent component) throws PDFNetException {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        PDFDoc pdfdoc = component.getPdfDoc();
        if (null == pdfViewCtrl || null == pdfdoc) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        boolean shouldUnlock = false;
        try {
            pdfViewCtrl.docLock(true);
            shouldUnlock = true;

            for (String fieldName : fieldNames) {
                Field field = pdfdoc.getField(fieldName);
                if (field != null && field.isValid()) {
                    field.setFlag(flag, flagValue);
                    pdfViewCtrl.update(field);
                }
            }
        } finally {
            if (shouldUnlock) {
                pdfViewCtrl.docUnlock();
            }
        }
        result.success(null);
    }

    private static void setValuesForFields(String fieldsString, MethodChannel.Result result, ViewerComponent component) throws PDFNetException, JSONException {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        PDFDoc pdfDoc = component.getPdfDoc();
        if (null == pdfViewCtrl || null == pdfDoc) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        JSONArray fieldsArray = new JSONArray(fieldsString);

        boolean shouldUnlock = false;
        try {
            pdfViewCtrl.docLock(true);
            shouldUnlock = true;

            for (int i = 0; i < fieldsArray.length(); i++) {
                JSONObject fieldObject = fieldsArray.getJSONObject(i);

                String fieldName = fieldObject.getString(KEY_FIELD_NAME);

                Field field = pdfDoc.getField(fieldName);
                if (field != null && field.isValid()) {
                    setFieldValue(pdfViewCtrl, field, fieldObject.get(KEY_FIELD_VALUE));
                }
            }
        } finally {
            if (shouldUnlock) {
                pdfViewCtrl.docUnlock();
            }
        }
        result.success(null);
    }

    // write lock required around this method
    private static void setFieldValue(PDFViewCtrl pdfViewCtrl, Field field, Object value) throws PDFNetException, JSONException {
        int fieldType = field.getType();

        if (value instanceof Boolean) {
            if (Field.e_check == fieldType) {
                ViewChangeCollection view_change = field.setValue((boolean) value);
                pdfViewCtrl.refreshAndUpdate(view_change);
            }
        } else if (value instanceof String) {
            if (Field.e_text == fieldType || Field.e_radio == fieldType || Field.e_choice == fieldType) {
                ViewChangeCollection view_change = field.setValue((String) value);
                pdfViewCtrl.refreshAndUpdate(view_change);
            }
        } else if (value instanceof Integer || value instanceof Double || value instanceof Long || value instanceof Float) {
            if (Field.e_text == fieldType) {
                ViewChangeCollection view_change = field.setValue(String.valueOf(value));
                pdfViewCtrl.refreshAndUpdate(view_change);
            }
        }
    }

    private static void setToolMode(String toolModeString, MethodChannel.Result result, ViewerComponent component) {
        ToolManager toolManager = component.getToolManager();
        Context context = component.getPdfViewCtrl() != null ? component.getPdfViewCtrl().getContext() : null;
        if (toolManager == null || context == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }

        // For multi-select rect and lasso tool, we need to set the selection mode on tool manager
        if (TOOL_ANNOTATION_LASSO.equals(toolModeString)) {
            toolManager.setMultiSelectMode(AnnotEditRectGroup.SelectionMode.LASSO);
        } else if (TOOL_ANNOTATION_EDIT.equals(toolModeString)) {
            toolManager.setMultiSelectMode(AnnotEditRectGroup.SelectionMode.RECTANGULAR);
        }

        // Create our tool
        ToolManager.ToolMode mode = convStringToToolMode(toolModeString);
        Tool tool = (Tool) toolManager.createTool(mode, null);
        boolean continuousAnnot = PdfViewCtrlSettingsManager.getContinuousAnnotationEdit(context);
        tool.setForceSameNextToolMode(continuousAnnot);
        toolManager.setTool(tool);
        result.success(null);
    }

    private static void closeAllTabs(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabHostFragment2 pdfViewCtrlTabHostFragment = component.getPdfViewCtrlTabHostFragment();
        if (pdfViewCtrlTabHostFragment == null) {
            result.error("InvalidState", "Activity not attached", null);
            return;
        }

        pdfViewCtrlTabHostFragment.closeAllTabs();
        result.success(null);
    }

    private static void deleteAllAnnotations(MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        PDFDoc pdfDoc = component.getPdfDoc();
        if (pdfViewCtrl == null || pdfDoc == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }
        boolean hasChange = false;
        boolean shouldUnlock = false;

        try {
            // Locks the document first as accessing annotation/doc information isn't thread
            // safe.
            pdfViewCtrl.docLock(true);
            shouldUnlock = true;

            AnnotUtils.safeDeleteAllAnnots(pdfDoc);
            pdfViewCtrl.update(true);
            hasChange = pdfDoc.hasChangesSinceSnapshot();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (shouldUnlock) {
                pdfViewCtrl.docUnlock();
            }
        }
        if (hasChange) {
            ToolManager toolManager = (ToolManager) pdfViewCtrl.getToolManager();
            if (toolManager != null) {
                toolManager.raiseAllAnnotationsRemovedEvent();
            }
        }
        result.success(null);
    }

    private static void getPageRotation(int pageNumber, MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        PDFDoc pdfDoc = component.getPdfDoc();
        if (pdfViewCtrl == null || pdfDoc == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }
        int pageRotation = 0;
        boolean shouldUnlockRead = false;
        try {
            // Locks the document first as accessing annotation/doc information isn't thread
            // safe.
            pdfViewCtrl.docLockRead();
            shouldUnlockRead = true;

            pageRotation = pdfDoc.getPage(pageNumber).getRotation() * 90;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (shouldUnlockRead) {
                pdfViewCtrl.docUnlockRead();
            }
        }
        result.success(pageRotation);
    }

    private static void rotateClockwise(MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
        }

        pdfViewCtrl.rotateClockwise();
        result.success(null);
    }

    private static void rotateCounterClockwise(MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
        }

        pdfViewCtrl.rotateCounterClockwise();
        result.success(null);
    }

    private static void exportAsImage(int pageNumber, int dpi, String exportFormat, MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        PDFDoc pdfDoc = component.getPdfDoc();
        if (pdfViewCtrl == null || pdfDoc == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
        }
        try {
            String imagePath = exportAsImageHelper(pdfDoc, pageNumber, dpi, exportFormat);
            result.success(imagePath);
        } catch (Exception e) {
            result.error(Long.toString(e.hashCode()), "Exception Error: " + e, null);
        }
    }

    private static void exportAsImageFromFilePath(int pageNumber, int dpi, String exportFormat, String path, MethodChannel.Result result, ViewerComponent component) {
        try {
            PDFDoc pdfDoc = new PDFDoc(path);
            String imagePath = exportAsImageHelper(pdfDoc, pageNumber, dpi, exportFormat);
            pdfDoc.close();
            result.success(imagePath);
        } catch (Exception e) {
            result.error(Long.toString(e.hashCode()), "Exception Error: " + e, null);
        }
    }

    private static String exportAsImageHelper(PDFDoc doc, int pageNumber, int dpi, String exportFormat) throws PDFNetException {
        PDFDraw draw = null;
        boolean shouldUnlockRead = false;
        try {
            doc.lockRead();
            shouldUnlockRead = true;

            draw = new PDFDraw();
            draw.setDPI(dpi);
            Page pg = doc.getPage(pageNumber);
            String ext = "png";
            if (KEY_EXPORT_FORMAT_BMP.equals(exportFormat)) {
                ext = "bmp";
            } else if (KEY_EXPORT_FORMAT_JPEG.equals(exportFormat)) {
                ext = "jpeg";
            }
            File tempFile = File.createTempFile("tmp", "." + ext);
            draw.export(pg, tempFile.getAbsolutePath(), exportFormat);
            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (draw != null) {
                try {
                    draw.destroy();
                } catch (Exception ignored) {
                }
            }
            if (shouldUnlockRead) {
                doc.unlockRead();
            }
        }
    }

    private static void gotoPreviousPage(MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }
        boolean pageChanged = pdfViewCtrl.gotoPreviousPage();
        result.success(pageChanged);
    }

    private static void gotoNextPage(MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }
        boolean pageChanged = pdfViewCtrl.gotoNextPage();
        result.success(pageChanged);
    }

    private static void gotoFirstPage(MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }
        boolean pageChanged = pdfViewCtrl.gotoFirstPage();
        result.success(pageChanged);
    }

    private static void gotoLastPage(MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }
        boolean pageChanged = pdfViewCtrl.gotoLastPage();
        result.success(pageChanged);
    }

    private static void addBookmark(String title, Integer pageNumber, MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        PDFDoc pdfDoc = component.getPdfDoc();
        if (pdfViewCtrl == null || pdfDoc == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }

        boolean shouldUnlock = false;
        try {
            pdfViewCtrl.docLock(true);
            shouldUnlock = true;

            String jsonString = BookmarkManager.exportPdfBookmarks(pdfDoc);
            JSONObject jsonObject = new JSONObject(jsonString);
            jsonObject.put(pageNumber.toString(), title);
            jsonString = jsonObject.toString();
            importBookmarkJson(jsonString, result, component);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (shouldUnlock) {
                pdfViewCtrl.docUnlock();
            }
        }
    }

    private static void setBackgroundColor(MethodCall call, MethodChannel.Result result, ViewerComponent component) throws PDFNetException {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();

        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }

        int red = call.argument(KEY_RED);
        int green = call.argument(KEY_GREEN);
        int blue = call.argument(KEY_BLUE);
        pdfViewCtrl.setClientBackgroundColor(red, green, blue, false);
        result.success(null);
    }

    private static void setDefaultPageColor(MethodCall call, MethodChannel.Result result, ViewerComponent component) throws PDFNetException {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();

        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }

        int red = call.argument(KEY_RED);
        int green = call.argument(KEY_GREEN);
        int blue = call.argument(KEY_BLUE);
        pdfViewCtrl.setDefaultPageColor(red, green, blue);
        result.success(null);
    }

    private static void getScrollPos(MethodChannel.Result result, ViewerComponent component) throws JSONException {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        JSONObject jsonObject = new JSONObject();

        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }

        jsonObject.put(SCROLL_HORIZONTAL, pdfViewCtrl.getHScrollPos());
        jsonObject.put(SCROLL_VERTICAL, pdfViewCtrl.getVScrollPos());

        result.success(jsonObject.toString());
    }

    private static void setHorizontalScrollPosition(MethodCall call, MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        int horizontalScrollPosition = call.argument(KEY_HORIZONTAL_SCROLL_POSITION);

        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }

        pdfViewCtrl.setHScrollPos(horizontalScrollPosition);
        result.success(null);
    }

    private static void setVerticalScrollPosition(MethodCall call, MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        int verticalScrollPosition = call.argument(KEY_VERTICAL_SCROLL_POSITION);

        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }

        pdfViewCtrl.setVScrollPos(verticalScrollPosition);
        result.success(null);
    }

    public static void startSearchMode(MethodCall call, MethodChannel.Result result, ViewerComponent component) throws JSONException {
        PdfViewCtrlTabFragment2 pdfViewCtrlTabFragment = component.getPdfViewCtrlTabFragment();
        String searchString = call.argument(KEY_SEARCH_STRING);
        boolean matchCase = call.argument(KEY_MATCH_CASE);
        boolean matchWholeWord = call.argument(KEY_MATCH_WHOLE_WORD);
        if (pdfViewCtrlTabFragment != null) {
            pdfViewCtrlTabFragment.setSearchMode(true);
            pdfViewCtrlTabFragment.setSearchQuery(searchString);
            pdfViewCtrlTabFragment.setSearchMatchCase(matchCase);
            pdfViewCtrlTabFragment.setSearchWholeWord(matchWholeWord);
            pdfViewCtrlTabFragment.queryTextSubmit(searchString);
            result.success(null);
        } else {
            result.error("InvalidState", "Activity not attached", null);
        }
    }

    public static void exitSearchMode(MethodChannel.Result result, ViewerComponent component) {
        PdfViewCtrlTabFragment2 pdfViewCtrlTabFragment = component.getPdfViewCtrlTabFragment();
        if (pdfViewCtrlTabFragment != null) {
            pdfViewCtrlTabFragment.setSearchMode(false);
            pdfViewCtrlTabFragment.cancelFindText();
            pdfViewCtrlTabFragment.exitSearchMode();
        } else {
            result.error("InvalidState", "Activity not attached", null);
        }
    }

    private static void zoomWithCenter(MethodCall call, MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }

        double zoom = call.argument(KEY_ZOOM);
        int x = call.argument(KEY_X);
        int y = call.argument(KEY_Y);
        pdfViewCtrl.setZoom(x, y, zoom);
        result.success(null);
    }

    private static void zoomToRect(MethodCall call, MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }

        try {
            int pageNumber = call.argument(KEY_PAGE_NUMBER);
            double x1 = call.argument(KEY_X1);
            double y1 = call.argument(KEY_Y1);
            double x2 = call.argument(KEY_X2);
            double y2 = call.argument(KEY_Y2);
            com.pdftron.pdf.Rect rect = new com.pdftron.pdf.Rect(x1, y1, x2, y2);
            pdfViewCtrl.showRect(pageNumber, rect);
            result.success(null);
        } catch (PDFNetException ex) {
            ex.printStackTrace();
            result.error(Long.toString(ex.getErrorCode()), "PDFTronException Error: " + ex, null);
        }
    }

    private static void getVisiblePages(MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }
        int[] visiblePages = pdfViewCtrl.getVisiblePages();
        result.success(visiblePages);
    }

    // Hygen Generated Methods
    public static void setLayoutMode(MethodCall call, MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        String layoutString = call.argument(KEY_LAYOUT_MODE);

        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }

        pdfViewCtrl.setPagePresentationMode(convStringToPagePresentationMode(layoutString));
        result.success(null);
    }

    public static void setFitMode(MethodCall call, MethodChannel.Result result, ViewerComponent component) {
        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        String fitString = call.argument(KEY_FIT_MODE);

        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }
        pdfViewCtrl.setPageViewMode(convStringToFitMode(fitString));
        result.success(null);
    }

    public static void getAnnotationsOnPage(MethodCall call, MethodChannel.Result result, ViewerComponent component) {
        int pageNumber = call.argument(KEY_PAGE_NUMBER);

        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            result.error("InvalidState", "PDFViewCtrl not found", null);
            return;
        }

        ArrayList<Annot> annotations = pdfViewCtrl.getAnnotationsOnPage(pageNumber);
        JSONArray resultAnnotations = new JSONArray();

        for (Annot annot : annotations) {
            String id = null;
            try {
                id = annot.getUniqueID() != null ? annot.getUniqueID().getAsPDFText() : null;
            } catch (PDFNetException e) {
                e.printStackTrace();
            }

            try {
                if (id != null) {
                    resultAnnotations.put(new JSONObject()
                            .put(KEY_ANNOTATION_ID, id)
                            .put(KEY_PAGE_NUMBER, pageNumber));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        result.success(resultAnnotations.toString());
    }

    // Events

    public static void handleDocumentLoaded(final ViewerComponent component) {
        if (component == null) {
            return;
        }
        // Set initial page number
        if (component.getInitialPageNumber() > 0 && component.getPdfViewCtrl() != null) {
            component.getPdfViewCtrl().setCurrentPage(component.getInitialPageNumber());
        }

        if (component.getPdfViewCtrlTabFragment() != null) {
            if (!component.isAutoSaveEnabled()) {
                component.getPdfViewCtrlTabFragment().setSavingEnabled(component.isAutoSaveEnabled());
            }
        }

        if (component.getToolManager() != null) {
            component.getToolManager().setStylusAsPen(component.isUseStylusAsPen());
            component.getToolManager().setSignSignatureFieldsWithStamps(component.isSignSignatureFieldWithStamps());
        }

        addListeners(component);

        MethodChannel.Result result = component.getFlutterLoadResult();
        if (result != null) {
            result.success(true);
        }

        if (component.getPdfViewCtrlTabFragment() != null) {
            EventChannel.EventSink documentLoadedEventSink = component.getDocumentLoadedEventEmitter();
            if (documentLoadedEventSink != null) {
                documentLoadedEventSink.success(component.getPdfViewCtrlTabFragment().getFilePath());
            }
        }

        if (component.isAnnotationManagerEnabled() && component.getUserId() != null) {
            component.getToolManager().enableAnnotManager(
                    component.getUserId(),
                    component.getUserName(),
                    mAnnotationManagerUndoMode,
                    mAnnotationManagerEditMode,
                    new AnnotManager.AnnotationSyncingListener() {
                        @Override
                        public void onLocalChange(String action, String xfdfCommand, String xfdfJSON) {
                            EventChannel.EventSink eventSink = component.getExportAnnotationCommandEventEmitter();
                            if (eventSink != null) {
                                eventSink.success(xfdfCommand);
                            }
                        }
                    });
        }
        handleCustomAppBarButton(component);
    }

    public static boolean handleOpenDocError(ViewerComponent component) {
        if (component == null) {
            return false;
        }
        MethodChannel.Result result = component.getFlutterLoadResult();
        if (result != null) {
            result.success(false);
        }

        if (component.getPdfViewCtrlTabFragment() != null) {
            EventChannel.EventSink documentErrorEventSink = component.getDocumentErrorEventEmitter();
            if (documentErrorEventSink != null) {
                documentErrorEventSink.success(null);
            }
        }

        return false;
    }
    public static void handleOnConfigurationChanged(ViewerComponent component) {
        if (component == null) {
            return;
        }
        handleCustomAppBarButton(component);
    }

    public static void handleOnDetach(ViewerComponent component) {
        MethodChannel.Result result = component.getFlutterLoadResult();
        if (result != null) {
            result.success(false);
        }

        ToolManager toolManager = component.getToolManager();
        if (toolManager != null) {
            component.getImpl().removeListeners(toolManager);
        }

        PdfViewCtrlTabFragment2 pdfViewCtrlTabFragment = component.getPdfViewCtrlTabFragment();
        if (pdfViewCtrlTabFragment != null) {
            component.getImpl().removeListeners(pdfViewCtrlTabFragment);

            PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
            if (pdfViewCtrl != null) {
                component.getImpl().removeListeners(pdfViewCtrl);
            }
        }

        ArrayList<File> tempFiles = component.getTempFiles();

        for (File file : tempFiles)
            if (file != null && file.exists()) {
                file.delete();
            }
        tempFiles.clear();
    }

    public static void handleLeadingNavButtonPressed(ViewerComponent component) {
        if (component == null) {
            return;
        }
        EventChannel.EventSink leadingNavButtonPressedEventSink = component.getLeadingNavButtonPressedEventEmitter();
        if (leadingNavButtonPressedEventSink != null) {
            leadingNavButtonPressedEventSink.success(null);
        }
    }

    public static void handleAnnotationCustomToolbarItemPressed(ViewerComponent component, MenuItem item) {
        if (component == null) {
            return;
        }
        EventChannel.EventSink annotationCustomToolbarItemPressedEventSink = component.getAnnotationToolbarItemPressedEventEmitter();
        int itemId = item.getItemId();
        String itemKey = mToolIdMap.get(itemId);
        if (itemKey != null && annotationCustomToolbarItemPressedEventSink != null) {
            // this is a custom button
            annotationCustomToolbarItemPressedEventSink.success(itemId);
        }
    }

    public static void handleAppBarButtonPressed(ViewerComponent component, MenuItem item) {
        if (component == null) {
            return;
        }
        EventChannel.EventSink appBarButtonPressedEventSink = component.getAppBarButtonPressedEventEmitter();
        int itemId = item.getItemId();
        if (appBarButtonPressedEventSink != null) {
            appBarButtonPressedEventSink.success(itemId);
        }
    }

    private static void handleCustomAppBarButton(ViewerComponent component) {
        if (component.getAppNavRightBarItems() != null) {
            PdfViewCtrlTabHostFragment2 hostFragment = component.getPdfViewCtrlTabHostFragment();
            if (hostFragment != null) {
                androidx.appcompat.widget.Toolbar toolbar = hostFragment.getToolbar();
                if (toolbar != null) {
                    toolbar.getMenu().clear();

                    try {
                        for (int i = 0; i < component.getAppNavRightBarItems().size(); i++) {
                            String object = component.getAppNavRightBarItems().get(i);

                            int itemId = 0;
                            String itemName = null, itemIcon = null;
                            JSONObject item = new JSONObject(object);
                            if (!item.isNull(TOOLBAR_ITEM_KEY_ID)) {
                                itemId = item.getInt(TOOLBAR_ITEM_KEY_ID);
                            }
                            if (!item.isNull(TOOLBAR_ITEM_KEY_NAME)) {
                                itemName = item.getString(TOOLBAR_ITEM_KEY_NAME);
                            }
                            if (!item.isNull(TOOLBAR_ITEM_KEY_ICON)) {
                                itemIcon = item.getString(TOOLBAR_ITEM_KEY_ICON);
                            }

                            if (itemId != 0 && itemName != null && !Utils.isNullOrEmpty(itemIcon)) {
                                int res = Utils.getResourceDrawable(toolbar.getContext(), itemIcon);
                                Menu navBarMenu = toolbar.getMenu();
                                if (navBarMenu != null) {
                                    navBarMenu.add(Menu.NONE, itemId, Menu.NONE, itemName)
                                            .setIcon(res)
                                            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void addListeners(ViewerComponent component) {
        ToolManager toolManager = component.getToolManager();

        if (toolManager != null) {
            component.getImpl().addListeners(toolManager);
        }

        component.getImpl().setActionInterceptCallback();

        PdfViewCtrlTabFragment2 pdfViewCtrlTabFragment = component.getPdfViewCtrlTabFragment();
        if (pdfViewCtrlTabFragment != null) {
            component.getImpl().addListeners(pdfViewCtrlTabFragment);

            PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
            if (pdfViewCtrl != null) {
                component.getImpl().addListeners(pdfViewCtrl);
            }
        }
    }

    public static void emitAnnotationChangedEvent(String action, Map<Annot, Integer> map, ViewerComponent component) {
        if (component == null) {
            return;
        }
        EventChannel.EventSink eventSink = component.getAnnotationChangedEventEmitter();
        if (eventSink != null) {
            JSONObject resultObject = new JSONObject();
            try {
                resultObject.put(KEY_ACTION, action);
                JSONArray annotArray = new JSONArray();
                for (Annot annot : map.keySet()) {
                    String uid = null;
                    try {
                        uid = annot.getUniqueID() != null ? annot.getUniqueID().getAsPDFText() : null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (uid != null) {
                        Integer pageNumber = map.get(annot);
                        JSONObject annotObject = new JSONObject();
                        annotObject.put(KEY_ANNOTATION_ID, uid);
                        annotObject.put(KEY_PAGE_NUMBER, pageNumber);
                        annotArray.put(annotObject);
                    }
                }
                resultObject.put(KEY_ANNOTATION_LIST, annotArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            eventSink.success(resultObject.toString());
        }
    }

    public static void emitExportAnnotationCommandEvent(String action, Map<Annot, Integer> map, ViewerComponent component) {
        if (component == null || component.getToolManager() != null && component.getToolManager().getAnnotManager() != null) {
            return;
        } else {
            ArrayList<Annot> annots = new ArrayList<>(map.keySet());
            String xfdfCommand = null;
            try {
                if (action.equals(KEY_ACTION_ADD)) {
                    xfdfCommand = generateXfdfCommand(annots, null, null, component);
                } else if (action.equals(KEY_ACTION_MODIFY)) {
                    xfdfCommand = generateXfdfCommand(null, annots, null, component);
                } else {
                    xfdfCommand = generateXfdfCommand(null, null, annots, component);
                }
            } catch (PDFNetException e) {
                e.printStackTrace();
            }

            EventChannel.EventSink eventSink = component.getExportAnnotationCommandEventEmitter();
            if (eventSink != null) {
                eventSink.success(xfdfCommand);
            }
        }
    }

    public static void emitAnnotationsSelectedEvent(Map<Annot, Integer> map, ViewerComponent component) {
        if (component == null) {
            return;
        }
        component.setSelectedAnnots(new HashMap<>(map));

        if (hasAnnotationsSelected(component)) {
            EventChannel.EventSink eventSink = component.getAnnotationsSelectedEventEmitter();
            if (eventSink != null) {
                JSONArray resultArray = null;
                try {
                    resultArray = getAnnotationsData(component);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                eventSink.success(resultArray == null ? "[]" : resultArray.toString());
            }
        }
    }

    // Helpers

    private static void checkFunctionPrecondition(ViewerComponent component) {
        Objects.requireNonNull(component);
        Objects.requireNonNull(component.getPdfDoc());
    }

    @Nullable
    private static String generateXfdfCommand(ArrayList<Annot> added, ArrayList<Annot> modified, ArrayList<Annot> removed, ViewerComponent component) throws PDFNetException {
        PDFDoc pdfDoc = component.getPdfDoc();
        if (pdfDoc != null) {
            FDFDoc fdfDoc = pdfDoc.fdfExtract(added, modified, removed);
            return fdfDoc.saveAsXFDF();
        }
        return null;
    }

    @Nullable
    public static String generateBookmarkJson(ViewerComponent component) throws JSONException {
        PDFDoc pdfDoc = component.getPdfDoc();
        if (pdfDoc != null) {
            return BookmarkManager.exportPdfBookmarks(pdfDoc);
        }
        return null;
    }

    private static JSONObject getAnnotationData(Annot annot, int pageNumber, ViewerComponent component) throws JSONException {

        // try to obtain id
        String uid = null;
        try {
            uid = annot.getUniqueID() != null ? annot.getUniqueID().getAsPDFText() : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (uid != null && component.getPdfViewCtrl() != null) {

            JSONObject annotPair = new JSONObject();
            annotPair.put(KEY_ANNOTATION_ID, uid);
            annotPair.put(KEY_PAGE_NUMBER, pageNumber);
            // try to obtain bbox
            try {
                com.pdftron.pdf.Rect bbox = component.getPdfViewCtrl().getScreenRectForAnnot(annot, pageNumber);
                JSONObject bboxMap = new JSONObject();
                bboxMap.put(KEY_X1, bbox.getX1());
                bboxMap.put(KEY_Y1, bbox.getY1());
                bboxMap.put(KEY_X2, bbox.getX2());
                bboxMap.put(KEY_Y2, bbox.getY2());
                bboxMap.put(KEY_WIDTH, bbox.getWidth());
                bboxMap.put(KEY_HEIGHT, bbox.getHeight());
                annotPair.put(KEY_RECT, bboxMap);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return annotPair;
        }

        return null;
    }

    public static JSONArray getAnnotationsData(ViewerComponent component) throws JSONException {
        JSONArray annots = new JSONArray();

        for (Map.Entry<Annot, Integer> entry : component.getSelectedAnnots().entrySet()) {
            Annot key = entry.getKey();
            Integer value = entry.getValue();

            JSONObject annotData = getAnnotationData(key, value, component);
            if (annotData != null) {
                annots.put(annotData);
            }
        }
        return annots;
    }

    public static boolean hasAnnotationsSelected(ViewerComponent component) {
        return component.getSelectedAnnots() != null && !component.getSelectedAnnots().isEmpty();
    }

    public static void checkQuickMenu(List<QuickMenuItem> menuItems, ArrayList<String> keepList, List<QuickMenuItem> removeList) {
        for (QuickMenuItem item : menuItems) {
            int menuId = item.getItemId();
            if (ToolConfig.getInstance().getToolModeByQMItemId(menuId) != null) {
                // skip real annotation tools
                return;
            }
            String menuStr = convQuickMenuIdToString(menuId);
            if (!keepList.contains(menuStr)) {
                removeList.add(item);
            }
        }
    }

    private static JSONArray getJSONArrayFromJSONObject(JSONObject jsonObject, String key) throws JSONException {
        String jsonArrayString = jsonObject.getString(key);
        return new JSONArray(jsonArrayString);
    }

    private static JSONObject getJSONObjectFromJSONObject(JSONObject jsonObject, String key) throws JSONException {
        String jsonObjectString = jsonObject.getString(key);
        return new JSONObject(jsonObjectString);
    }

    private static ArrayList<String> convertJSONArrayToArrayList(JSONArray jsonArray) throws JSONException {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            arrayList.add(jsonArray.getString(i));
        }
        return arrayList;
    }

    private static com.pdftron.pdf.Rect getRectFromObject(Object object) throws JSONException, PDFNetException {
        if (object instanceof String) {
            JSONObject rectJson = new JSONObject((String) object);
            if (!rectJson.isNull(KEY_X1) && !rectJson.isNull(KEY_Y1) && !rectJson.isNull(KEY_X2) && !rectJson.isNull(KEY_Y2)) {

                double rectX1 = rectJson.getDouble(KEY_X1);
                double rectY1 = rectJson.getDouble(KEY_Y1);
                double rectX2 = rectJson.getDouble(KEY_X2);
                double rectY2 = rectJson.getDouble(KEY_Y2);

                return new com.pdftron.pdf.Rect(rectX1, rectY1, rectX2, rectY2);
            }
        }
        return null;
    }

    // Hatn
    private static void setThemeMode(ViewerComponent component, boolean isDarkMode) throws PDFNetException {
        PdfViewCtrlTabHostFragment2 pdfViewCtrlTabHostFragment = component.getPdfViewCtrlTabHostFragment();
        if (pdfViewCtrlTabHostFragment == null) {
            return;
        }

        PDFViewCtrl pdfViewCtrl = component.getPdfViewCtrl();
        if (pdfViewCtrl == null) {
            return;
        }
        Context context = pdfViewCtrl.getContext();

        int mode = isDarkMode ? PdfViewCtrlSettingsManager.KEY_PREF_COLOR_MODE_NIGHT : PdfViewCtrlSettingsManager.KEY_PREF_COLOR_MODE_NORMAL;
        PdfViewCtrlSettingsManager.setColorMode(context, mode);

        pdfViewCtrlTabHostFragment.updateColorMode();
    }
}
