<%@ include file="/include-internal.jsp"%>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%--
    /*
    *    This file is part of TeamCity Stash.
    *
    *    TeamCity Stash is free software: you can redistribute it and/or modify
    *    it under the terms of the GNU General Public License as published by
    *    the Free Software Foundation, either version 3 of the License, or
    *    (at your option) any later version.
    *
    *    TeamCity Stash is distributed in the hope that it will be useful,
    *    but WITHOUT ANY WARRANTY; without even the implied warranty of
    *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    *    GNU General Public License for more details.
    *
    *    You should have received a copy of the GNU General Public License
    *    along with TeamCity Stash.  If not, see <http://www.gnu.org/licenses/>.
    */
  --%>

<jsp:useBean id="keys" class="mendhak.teamcity.stash.ui.StashServerKeyNames"/>

<tr>
  <td colspan="2">Specify Stash repository name and credentials to push status updates to</td>
</tr>
<l:settingsGroup title="Authentication">
<tr>
  <th>URL:<l:star/></th>
  <td>
    <props:textProperty name="${keys.serverKey}" className="longField"/>
    <span class="error" id="error_${keys.serverKey}"></span>
    <span class="smallNote">Specify Stash URL</span>
  </td>
</tr>
<tr>
  <th>User Name:<l:star/></th>
  <td>
    <props:textProperty name="${keys.userNameKey}" className="longField"/>
    <span class="error" id="error_${keys.userNameKey}"></span>
    <span class="smallNote">Specify Stash username</span>
  </td>
</tr>
<tr>
  <th>Password:<l:star/></th>
  <td>
    <props:passwordProperty name="${keys.passwordKey}" className="longField"/>
    <span class="error" id="error_${keys.passwordKey}"></span>
    <span class="smallNote">Specify Stash password</span>
  </td>
</tr>
<tr>
  <th>Additional headers:</th>
  <td>
    <props:textProperty name="${keys.headersKey}" className="longField"/>
    <span class="smallNote">Specify additional comma-separated headers (e.g. X-Header-1: value1, X-Header-2: value2)</span>
  </td>
</tr>
</l:settingsGroup>
<l:settingsGroup title="Other">
<tr>
  <th>Canceled Builds:</th>
  <td>
    <props:checkboxProperty name="${keys.failCancelledBuilds}" />
    <label for="${keys.failCancelledBuilds}">Mark cancelled builds as failed</label>
    <span class="smallNote">Stash has no canceled status, choose to send it as pass or failed</span>
  </td>
</tr>
<tr>
  <th>Stash history:</th>
  <td>
    <props:checkboxProperty name="${keys.onlyLatestKey}" />
    <label for="${keys.onlyLatestKey}">Only show latest build status for each commit</label>
    <span class="smallNote">If checked, only the latest build status of each commit appears in Stash</span>
  </td>
</tr>
<tr>
  <th>Ignore VCS Roots:</th>
  <td>
    <props:textProperty name="${keys.VCSIgnoreKey}" className="longField"/>
    <span class="error" id="error_${keys.VCSIgnoreKey}"></span>
    <span class="smallNote">(Optional) VCS root names to ignore, comma separated</span>
  </td>
</tr>
</l:settingsGroup>
