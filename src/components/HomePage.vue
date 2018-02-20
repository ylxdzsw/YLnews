<template>
  <v-ons-page>
    <v-ons-splitter>
      <v-ons-splitter-side swipeable collapse width="250px" :open.sync="sideMenuOpen">
        <setting-menu />
      </v-ons-splitter-side>

      <v-ons-splitter-content>
        <news-list @toggleSetting="sideMenuOpen = !sideMenuOpen" />
      </v-ons-splitter-content>
    </v-ons-splitter>
  </v-ons-page>
</template>

<script>
import NewsList from "./homepage/NewsList"
import SettingMenu from "./homepage/SettingMenu"

export default {
  data () {
    return {
      sideMenuOpen: false
    }
  },
  components: { NewsList, SettingMenu }
}
</script>
